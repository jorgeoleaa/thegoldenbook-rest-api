package com.thegoldenbook.repository;

import com.thegoldenbook.entity.LanguageEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.util.Assert;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LanguageRepositoryTest {

    @Autowired
    private LanguageRepository languageRepository;

    @Test
    void testFindByIso639() {
        Optional<LanguageEntity> languageEntity = languageRepository.findByIso639("en");
        Assert.isTrue(languageEntity.isPresent(), "No language found for iso639 'en'");
        Assert.isTrue(languageEntity.get().getIso639().contains("en"), "Unexpected iso639 value");
    }
}
