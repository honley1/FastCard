package com.honley.fastcard.service;

import com.honley.fastcard.entity.BusinessCardEntity;
import com.honley.fastcard.repository.BusinessCardRepository;
import com.honley.fastcard.repository.UserRepository;
import com.honley.fastcard.response.ResponseWithData;
import com.honley.fastcard.response.ResponseWithMessage;
import com.honley.fastcard.utils.GetObjects;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class BusinessCardService {

    private final UserRepository userRepository;
    private final BusinessCardRepository businessCardRepository;

    public ResponseEntity<?> getBusinessCard(String username) {
        if (!businessCardRepository.findByUser(
                userRepository.findByUsername(username).get()).get().getIsActivated()) {
            ResponseWithMessage response = new ResponseWithMessage(false, "Business card is not activated");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        if (businessCardRepository.findByUser(userRepository.findByUsername(username).get()).isEmpty()) {
            ResponseWithMessage response = new ResponseWithMessage(false, "Business card not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        BusinessCardEntity businessCard = businessCardRepository.findByUser(
                userRepository.findByUsername(username).get()).get();

        return ResponseEntity.ok(new ResponseWithData<>(true, GetObjects.getBusinessCardObject(businessCard)));
    }

    public ResponseEntity<?> activateBusinessCard(String username) {
        if (userRepository.findByUsername(username).isEmpty()) {
            ResponseWithMessage response = new ResponseWithMessage(false, "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (businessCardRepository.findByUser(userRepository.findByUsername(username).get()).isEmpty()) {
            ResponseWithMessage response = new ResponseWithMessage(false, "Business card not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        BusinessCardEntity businessCard = businessCardRepository.findByUser(
                userRepository.findByUsername(username).get()).get();

        businessCard.setIsActivated(true);
        businessCardRepository.save(businessCard);

        return ResponseEntity.ok(new ResponseWithData<>(true, GetObjects.getBusinessCardObject(businessCard)));
    }

    public ResponseEntity<?> deleteBusinessCard(String username) {
        if (userRepository.findByUsername(username).isEmpty()) {
            ResponseWithMessage response = new ResponseWithMessage(false, "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (businessCardRepository.findByUser(userRepository.findByUsername(username).get()).isEmpty()) {
            ResponseWithMessage response = new ResponseWithMessage(false, "Business card not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        BusinessCardEntity businessCard = businessCardRepository.findByUser(
                userRepository.findByUsername(username).get()).get();

        businessCardRepository.delete(businessCard);

        return ResponseEntity.ok(new ResponseWithData<>(false, GetObjects.getBusinessCardObject(businessCard)));
    }

    public ResponseEntity<?> updateBusinessCard(String username, String html, String css) {
        if (userRepository.findByUsername(username).isEmpty()) {
            ResponseWithMessage response = new ResponseWithMessage(false, "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (businessCardRepository.findByUser(userRepository.findByUsername(username).get()).isEmpty()) {
            ResponseWithMessage response = new ResponseWithMessage(false, "Business card not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        BusinessCardEntity businessCard = businessCardRepository.findByUser(
                userRepository.findByUsername(username).get()).get();

        businessCard.setHtml(html);
        businessCard.setCss(css);

        businessCardRepository.save(businessCard);


        return ResponseEntity.ok(new ResponseWithData<>(true, GetObjects.getBusinessCardObject(businessCard)));
    }
}
