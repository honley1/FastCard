package com.honley.fastcard.service;

import com.honley.fastcard.DTO.RegisterUserDTO;
import com.honley.fastcard.DTO.TokenDTO;
import com.honley.fastcard.DTO.UserDTO;
import com.honley.fastcard.entity.BusinessCardEntity;
import com.honley.fastcard.entity.PasswordResetTokenEntity;
import com.honley.fastcard.entity.RoleEntity;
import com.honley.fastcard.entity.UserEntity;
import com.honley.fastcard.repository.BusinessCardRepository;
import com.honley.fastcard.repository.PasswordResetTokenRepository;
import com.honley.fastcard.repository.RoleRepository;
import com.honley.fastcard.repository.UserRepository;
import com.honley.fastcard.response.ResponseWithData;
import com.honley.fastcard.response.ResponseWithMessage;
import com.honley.fastcard.utils.EmailValidator;
import com.honley.fastcard.utils.GetObjects;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final BusinessCardRepository businessCardRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderService mailSenderService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User '%s' not found", username)
        ));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }

    public ResponseEntity<?> createNewUser(@RequestBody RegisterUserDTO registerUserDTO) throws MessagingException {
        if (registerUserDTO.getPassword().length() < 8) {
            ResponseWithMessage response = new ResponseWithMessage(false, "Password is too short");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if (userRepository.findByUsername(registerUserDTO.getUsername()).isPresent()) {
            ResponseWithMessage response = new ResponseWithMessage(false, "Username already taken");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        if (userRepository.findByEmail(registerUserDTO.getEmail()).isPresent()) {
            ResponseWithMessage response = new ResponseWithMessage(false, "Email already taken");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        if (!EmailValidator.validateEmail(registerUserDTO.getEmail())) {
            ResponseWithMessage response = new ResponseWithMessage(false, "Invalid email format");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Set<RoleEntity> roles = new HashSet<>();

        RoleEntity userRole = roleRepository.findById(2L).get();
        roles.add(userRole);

        String username = registerUserDTO.getUsername();
        String email = registerUserDTO.getEmail();
        String password = passwordEncoder.encode(registerUserDTO.getPassword());
        String activationLink = UUID.randomUUID().toString();

        UserEntity user = userRepository.save(UserEntity.builder()
                .username(username)
                .email(email)
                .password(password)
                .roles(roles)
                .activationLink(activationLink)
                .isActivated(false)
                .build());

        BusinessCardEntity businessCard = businessCardRepository.save(BusinessCardEntity.builder()
                .html("")
                .css("")
                .user(user)
                .isActivated(false)
                .build());

        mailSenderService.sendActivationMail(user.getEmail());

        return ResponseEntity.ok(new ResponseWithData<>(true, GetObjects.getUserObject(user, businessCard)));
    }

    public ResponseEntity<?> activateUser(String activationLink) {
        if (userRepository.findByActivationLink(activationLink).isEmpty()) {
            ResponseWithMessage response = new ResponseWithMessage(false, "Incorrect activation link");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if (userRepository.existsByActivationLinkAndIsActivatedTrue(activationLink)) {
            ResponseWithMessage response = new ResponseWithMessage(false, "Account has already been activated");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        UserEntity user = userRepository.findByActivationLink(activationLink).get();

        user.setIsActivated(true);
        userRepository.save(user);

        return ResponseEntity.ok(new ResponseWithData<>(true, GetObjects.getUserObject(user, user.getBusinessCard())));
    }

    public ResponseEntity<?> getUserByUsername(String username) {
        if (userRepository.findByUsername(username).isEmpty()) {
            ResponseWithMessage response = new ResponseWithMessage(false, "User not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        UserEntity user = userRepository.findByUsername(username).get();

        return ResponseEntity.ok(new ResponseWithData<>(true, GetObjects.getUserObject(user, user.getBusinessCard())));
    }

    public Boolean isUserActivated(String authUsername) {
        return userRepository.findByUsername(authUsername).get().getIsActivated();
    }

    public ResponseEntity<?> getAllUser() {
        List<UserEntity> users = userRepository.findAll();
        List<UserDTO> userDTOS = new ArrayList<>();

        for (UserEntity user: users) {
            userDTOS.add(GetObjects.getUserObject(user, user.getBusinessCard()));
        }

        return ResponseEntity.ok(new ResponseWithData<>(true, userDTOS));
    }

    public ResponseEntity<?> generateResetToken(String username) throws MessagingException {
        if (userRepository.findByUsername(username).isEmpty()) {
            ResponseWithMessage response = new ResponseWithMessage(false, "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        UserEntity user = userRepository.findByUsername(username).get();
        String token = UUID.randomUUID().toString();

        PasswordResetTokenEntity tokenEntity = passwordResetTokenRepository.save(PasswordResetTokenEntity.builder()
                .token(token)
                .user(user)
                .build());

        mailSenderService.sendResetPasswordMail(user.getEmail());

        return ResponseEntity.ok(new ResponseWithData<>(true, TokenDTO.builder()
                .id(tokenEntity.getId())
                .token(tokenEntity.getToken())
                .user(GetObjects.getUserObject(tokenEntity.getUser(), tokenEntity.getUser().getBusinessCard()))
                .build()));
    }

    public ResponseEntity<?> updatePassword(String token, String newPassword) {
        if (passwordResetTokenRepository.findByToken(token).isEmpty()) {
            ResponseWithMessage response = new ResponseWithMessage(false, "Invalid or expired token");
            return ResponseEntity.ok(response);
        }

        PasswordResetTokenEntity resetToken = passwordResetTokenRepository.findByToken(token).get();
        UserEntity user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);

        return ResponseEntity.ok(new ResponseWithData<>(true, GetObjects.getUserObject(user, user.getBusinessCard())));
    }

    public ResponseEntity<?> deleteUser(String username) {
        if (userRepository.findByUsername(username).isEmpty()) {
            ResponseWithMessage response = new ResponseWithMessage(false, "User not found");
            return ResponseEntity.badRequest().body(response);
        }

        UserEntity user = userRepository.findByUsername(username).get();

        user.getRoles().clear();
        userRepository.save(user);
        userRepository.delete(user);

        return ResponseEntity.ok(new ResponseWithData<>(true, GetObjects.getUserObject(user, user.getBusinessCard())));
    }
}
