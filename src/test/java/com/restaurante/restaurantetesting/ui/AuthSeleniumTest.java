package com.restaurante.restaurantetesting.ui;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthSeleniumTest extends BaseSeleniumTest {

    @Test //Este codigo lo reutilizamos en el base para que el resto de test que necesitan loguearse se pueda loguar
    void login(){
        //primero debemos ubicar el input con el username
        driver.get(baseUrl + "login");
        driver.findElement(By.id("username")).sendKeys(user.getUsername());
        driver.findElement(By.id("password")).sendKeys("user");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(driver -> driver.getCurrentUrl().equals(baseUrl + "restaurants"));

        assertEquals(baseUrl + "restaurants", driver.getCurrentUrl());
    }
    @Test
    void register(){
        driver.get(baseUrl + "register");
        driver.findElement(By.id("username")).sendKeys("testuser");
        driver.findElement(By.id("email")).sendKeys("testuser@gmail.com");
        driver.findElement(By.id("password")).sendKeys("testuser");
        driver.findElement(By.id("passwordConfirm")).sendKeys("testuser");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(driver -> driver.getCurrentUrl().equals(baseUrl + "login"));
        assertEquals(baseUrl + "login", driver.getCurrentUrl());

    }
}
