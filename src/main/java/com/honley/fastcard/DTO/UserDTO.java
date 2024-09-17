package com.honley.fastcard.DTO;

import com.honley.fastcard.entity.RoleEntity;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Builder
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private Boolean isActivated;
    private Collection<RoleEntity> roles;
    private LocalDateTime createdAt;
    private BusinessCardDTO businessCard;
}
