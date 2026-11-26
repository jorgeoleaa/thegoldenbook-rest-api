package com.thegoldenbook.repository;

import com.thegoldenbook.entity.LanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface LanguageRepository extends JpaRepository<LanguageEntity, Integer> {

    Optional<LanguageEntity> findByIso639(String iso365);
}
