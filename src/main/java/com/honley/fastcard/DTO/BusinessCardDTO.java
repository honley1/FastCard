package com.honley.fastcard.DTO;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class BusinessCardDTO {

    private Long id;
    private String html;
    private String css;
    private Boolean isActivated;
    private LocalDateTime createdAt;
    private Long userId;
    private Long applicationId;
}
