package com.thegoldenbook.repository;

import com.thegoldenbook.entity.LanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LanguageRepository extends JpaRepository<LanguageEntity, Integer> {

    Optional<LanguageEntity> findByIso639(String iso365);
}
