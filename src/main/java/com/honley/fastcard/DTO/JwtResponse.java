package com.honley.fastcard.DTO;

import com.honley.fastcard.entity.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private Collection<RoleEntity> role;
}