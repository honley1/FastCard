package com.honley.fastcard.repository;

import com.honley.fastcard.entity.ApplicationEntity;
import com.honley.fastcard.entity.BusinessCardEntity;
import com.honley.fastcard.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {
    Optional<ApplicationEntity> findByUser(UserEntity user);
}
