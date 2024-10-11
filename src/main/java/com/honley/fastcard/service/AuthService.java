package com.honley.fastcard.service;

import com.honley.fastcard.DTO.JwtRequest;
import com.honley.fastcard.DTO.JwtResponse;
import com.honley.fastcard.repository.UserRepository;
import com.honley.fastcard.response.ResponseWithMessage;
import com.honley.fastcard.utils.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@AllArgsConstructor
public class AuthService {

    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserService userService;

    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            ResponseWithMessage response = new ResponseWithMessage(false, "Incorrect login or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token, userRepository.findByUsername(authRequest.getUsername()).get().getRoles())  );
    }
}
