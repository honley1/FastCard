package com.honley.fastcard.utils;

import com.honley.fastcard.DTO.ApplicationDTO;
import com.honley.fastcard.DTO.BusinessCardDTO;
import com.honley.fastcard.DTO.UserDTO;
import com.honley.fastcard.entity.ApplicationEntity;
import com.honley.fastcard.entity.BusinessCardEntity;
import com.honley.fastcard.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class GetObjects {

    public static UserDTO getUserObject(UserEntity user, BusinessCardEntity businessCard) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .isActivated(user.getIsActivated())
                .roles(user.getRoles())
                .createdAt(user.getCreatedAt())
                .businessCard(
                        BusinessCardDTO.builder()
                        .id(businessCard.getId())
                        .html(businessCard.getHtml())
                        .css(businessCard.getCss())
                        .isActivated(businessCard.getIsActivated())
                        .createdAt(businessCard.getCreatedAt())
                        .userId(businessCard.getUser().getId())
                        .build())
                .build();
    }

    public static BusinessCardDTO getBusinessCardObject(BusinessCardEntity businessCard) {
        return BusinessCardDTO.builder()
                .id(businessCard.getId())
                .html(businessCard.getHtml())
                .css(businessCard.getCss())
                .isActivated(businessCard.getIsActivated())
                .createdAt(businessCard.getCreatedAt())
                .userId(businessCard.getUser().getId())
                .build();
    }

    public static ApplicationDTO getApplicationObject(ApplicationEntity application) {
        return ApplicationDTO.builder()
                .id(application.getId())
                .fullName(application.getFullName())
                .phoneNumber(application.getPhoneNumber())
                .userId(application.getUser().getId())
                .businessCardId(application.getBusinessCard().getId())
                .build();
    }
}
