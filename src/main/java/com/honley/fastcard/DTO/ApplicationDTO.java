package com.honley.fastcard.DTO;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ApplicationDTO {

    private Long id;
    private String fullName;
    private String phoneNumber;
    private String html;
    private String css;
    private LocalDateTime createdAt;
    private Long userId;
    private Long businessCardId;
}
