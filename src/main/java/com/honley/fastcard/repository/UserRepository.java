package com.honley.fastcard.repository;

import com.honley.fastcard.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByUsername(String username);
    Boolean existsByActivationLinkAndIsActivatedTrue(String activationLink);
    Optional<UserEntity> findByActivationLink(String activationLink);
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByPassword(String password);
}
