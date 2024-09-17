package com.honley.fastcard.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateApplicationDTO {
    private String fullName;
    private String phoneNumber;
}
