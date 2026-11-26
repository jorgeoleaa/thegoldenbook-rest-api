package com.thegoldenbook.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Language")
public class LanguageEntity {

    @Id
    private Integer id;
    private String iso639;
    private String locale;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIso639() {
        return iso639;
    }

    public void setIso639(String iso639) {
        this.iso639 = iso639;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
