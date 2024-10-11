package com.honley.fastcard.service;

import com.honley.fastcard.DTO.CreateApplicationDTO;
import com.honley.fastcard.entity.ApplicationEntity;
import com.honley.fastcard.entity.ApplicationStatus;
import com.honley.fastcard.entity.BusinessCardEntity;
import com.honley.fastcard.entity.UserEntity;
import com.honley.fastcard.repository.ApplicationRepository;
import com.honley.fastcard.repository.BusinessCardRepository;
import com.honley.fastcard.repository.UserRepository;
import com.honley.fastcard.response.ResponseWithData;
import com.honley.fastcard.response.ResponseWithMessage;
import com.honley.fastcard.utils.GetObjects;
import com.honley.fastcard.utils.SendApplication;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final BusinessCardRepository businessCardRepository;
    private final SendApplication sendApplication;

    public ResponseEntity<?> createApplication(String username, CreateApplicationDTO applicationDTO) {
        if (userRepository.findByUsername(username).isEmpty()) {
            ResponseWithMessage response = new ResponseWithMessage(false, "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (applicationRepository.findByUser(userRepository.findByUsername(username).get()).isPresent()) {
            ResponseWithMessage response = new ResponseWithMessage(false, "Application already exist");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        String fullName = applicationDTO.getFullName();
        String phoneNumber = applicationDTO.getPhoneNumber();

        UserEntity user = userRepository.findByUsername(username).get();
        BusinessCardEntity businessCard = user.getBusinessCard();

        ApplicationEntity application = applicationRepository.save(
                ApplicationEntity.builder()
                        .fullName(fullName)
                        .phoneNumber(phoneNumber)
                        .user(user)
                        .businessCard(businessCard)
                        .build());

        businessCard.setApplication(application);
        businessCardRepository.save(businessCard);
        sendApplication.sendApplication(application.getFullName(),
                                        application.getPhoneNumber(),
                                        application.getUser().getUsername());

        return ResponseEntity.ok(new ResponseWithData<>(true, GetObjects.getApplicationObject(application)));
    }

    public ResponseEntity<?> closeApplication(String username) {
        if (userRepository.findByUsername(username).isEmpty()) {
            ResponseWithMessage response = new ResponseWithMessage(false, "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (applicationRepository.findByUser(userRepository.findByUsername(username).get()).isEmpty()) {
            ResponseWithMessage response = new ResponseWithMessage(false, "Application not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        ApplicationEntity application = applicationRepository.findByUser(
                userRepository.findByUsername(username).get()).get();

        application.setStatus(ApplicationStatus.CLOSED);
        applicationRepository.save(application);

        return ResponseEntity.ok(new ResponseWithData<>(false, GetObjects.getApplicationObject(application)));
    }
}
