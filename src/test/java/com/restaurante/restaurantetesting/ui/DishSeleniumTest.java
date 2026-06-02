package com.restaurante.restaurantetesting.ui;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class DishSeleniumTest extends BaseSeleniumTest{
    //dishList
    @Test
    void dishList(){
        driver.get(baseUrl + "dishes");
        assertEquals("Platos", driver.findElement(By.tagName("h1")).getText());
        assertTrue(driver.findElement(By.id("resultsCount")).getText().contains("plato"));

        List<WebElement> dishes = driver.findElements(By.cssSelector("#dishGrid .card"));
        assertTrue(dishes.size() >= 2);

        WebElement firstDish = dishes.getFirst();
        assertTrue(firstDish.getText().contains(pizza.getName()));
        assertTrue(firstDish.getText().contains(pizza.getPrice().toString().replace(".",",")));
        assertTrue(firstDish.getText().contains("Principales"));
        assertTrue(firstDish.getText().contains(pizza.getDescription()));
        assertTrue(firstDish.getText().contains(pizza.getRestaurant().getName()));

        firstDish.findElement(By.linkText("Ver")).click();

        assertEquals(baseUrl + "dishes/" + pizza.getId(), driver.getCurrentUrl());
    }

    //dishDetail
    @Test
    void dishDetail(){
        driver.get(baseUrl + "dishes/" + pizza.getId());
        assertEquals("Plato " + pizza.getId(), driver.findElement(By.tagName("h1")).getText());

        WebElement dish = driver.findElement(By.id("dishInfoCard"));
        assertTrue(dish.getText().contains(pizza.getName()));
        assertTrue(dish.getText().contains(pizza.getPrice().toString().replace(".", ",")));
        assertTrue(dish.getText().contains("Principales"));
        assertTrue(dish.getText().contains(pizza.getDescription()));
        assertTrue(dish.getText().contains(pizza.getRestaurant().getName()));

        List<WebElement> reviews = driver.findElements(By.cssSelector("#reviewsGrid .card"));
        assertFalse(reviews.isEmpty());
        WebElement firstReview = reviews.getFirst();
        assertEquals(pizzaOK.getTitle(), firstReview.findElement(By.tagName("h5")).getText());
        assertEquals(pizzaOK.getTitle(), firstReview.findElement(By.cssSelector(".card-title")).getText());
        assertEquals(pizzaOK.getContent(), firstReview.findElement(By.cssSelector(".card-text")).getText());
        assertEquals("5/5", firstReview.findElement(By.className("review-rating")).getText());
    }

    //reviews


    //(luego testing de login, de registro y luuuuuego el testing de formulairo, luego los pedidos y los distintos formularios)
}
