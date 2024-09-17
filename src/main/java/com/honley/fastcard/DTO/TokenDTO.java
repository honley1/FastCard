package com.honley.fastcard.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDTO {

    private Long id;
    private String token;
    private UserDTO user;
}
