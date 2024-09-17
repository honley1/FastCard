package com.honley.fastcard.repository;

import com.honley.fastcard.entity.BusinessCardEntity;
import com.honley.fastcard.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessCardRepository extends JpaRepository<BusinessCardEntity, Long> {
    Optional<BusinessCardEntity> findByUser(UserEntity user);
}
