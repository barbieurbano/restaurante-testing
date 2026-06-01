package com.restaurante.restaurantetesting.ui;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RestaurantSeleniumTest extends BaseSeleniumTest {

    @Test
    void restaurantList () {
        driver.get(baseUrl + "restaurants");

        assertEquals("Bienvenido a la lista de restaurantes", driver.findElement(By.tagName("h1")).getText());

        assertTrue(driver.findElement(By.id("resultsCount")).getText().contains("resultado"));

        List<WebElement> restaurants = driver.findElements(By.className("card-restaurant"));
        assertFalse(restaurants.isEmpty());

        WebElement firstRestaurant = restaurants.getFirst();
        assertTrue(firstRestaurant.getText().contains(pizzeria.getName()));
        assertTrue(firstRestaurant.getText().contains(pizzeria.getAveragePrice().toString()));
        assertTrue(firstRestaurant.getText().contains("Todo tipo"));

        // Como no es admin no puede ver el botón de Editar
        assertTrue(firstRestaurant.findElements(By.linkText("Editar")).isEmpty());

        WebElement viewBtn = firstRestaurant.findElement(By.linkText("Ver"));
        viewBtn.click();

        assertEquals(baseUrl + "restaurants/" + pizzeria.getId(), driver.getCurrentUrl());
    }

    @Test
    void restaurantDetail () {
        driver.get(baseUrl + "restaurants/" +  pizzeria.getId());

        // info restaurante
        assertEquals("Restaurante " + pizzeria.getId(), driver.findElement(By.tagName("h1")).getText());
        assertEquals(pizzeria.getName(), driver.findElement(By.id("restaurantName")).getText());
        assertTrue(driver.findElement(By.id("activeTrue")).getText().contains("Abierto"));

        // platos
        List<WebElement> dishes = driver.findElements(By.cssSelector("#dishesTable tbody tr"));
        assertTrue(dishes.size() >= 2);
        assertTrue(dishes.getFirst().getText().contains(pizza.getName()));
        assertTrue(dishes.get(1).getText().contains(tiramisu.getName()));

        // reviews
        List<WebElement> reviews = driver.findElements(By.cssSelector("#reviewsGrid .card"));
        assertTrue(reviews.size() >= 2);
        WebElement firstReview = reviews.getFirst();
        assertEquals(pizzeriaOK.getTitle(), firstReview.findElement(By.tagName("h5")).getText());
        assertEquals(pizzeriaOK.getTitle(), firstReview.findElement(By.cssSelector(".card-title")).getText());
        WebElement secondReview = reviews.get(1);
        assertEquals(pizzeriaMal.getTitle(), secondReview.findElement(By.tagName("h5")).getText());
        assertEquals(pizzeriaMal.getContent(), secondReview.findElement(By.cssSelector(".card-text")).getText());
        assertEquals("1/5", secondReview.findElement(By.className("review-rating")).getText());
    }
}
