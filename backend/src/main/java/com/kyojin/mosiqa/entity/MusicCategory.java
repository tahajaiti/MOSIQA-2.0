package com.kyojin.mosiqa.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MusicCategory {
    POP("pop"),
    ROCK("rock"),
    RAP("rap"),
    JAZZ("jazz"),
    CLASSICAL("classical"),
    ELECTRONIC("electronic"),
    RNB("rnb"),
    COUNTRY("country"),
    METAL("metal"),
    INDIE("indie"),
    OTHER("other");

    private final String value;

    MusicCategory(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static MusicCategory fromValue(String value) {
        for (MusicCategory category : MusicCategory.values()) {
            if (category.value.equalsIgnoreCase(value) || category.name().equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown category: " + value);
    }
}
