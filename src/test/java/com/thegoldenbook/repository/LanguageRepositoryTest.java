package com.thegoldenbook.repository;

import com.thegoldenbook.entity.LanguageEntity;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LanguageRepositoryTest {

    private Logger logger = LoggerFactory.getLogger(LanguageRepositoryTest.class);

    @Autowired
    private LanguageRepository languageRepository;

    @Test
    void testFindByIso639() {
        Optional<LanguageEntity> languageEntity = languageRepository.findByIso639("en");
        Assert.isTrue(languageEntity.isPresent(), "No language found for iso639 'en'");
        Assert.isTrue(languageEntity.get().getIso639().contains("en"), "Unexpected iso639 value");
    }

    @Test
    void testFindAll() {
        List<LanguageEntity> languages = languageRepository.findAll();

        if (languages.isEmpty()){
            logger.info("There are no results.");
        }else {
            for(LanguageEntity languageEntity : languages) {
                logger.info("Received languages: "+languageEntity.toString());
            }
        }
    }
}
