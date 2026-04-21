package com.restaurante.restaurantetesting.model.enums;
import lombok.Getter;
@Getter
public enum FoodType {
    SPANISH("Espanola"), JAPANESE("Japonesa"), MEXICAN("Mexicana"),ARGENTINIAN("Argentina ***");

    private final String label;

    FoodType(String label) {
        this.label = label;
    }
}
