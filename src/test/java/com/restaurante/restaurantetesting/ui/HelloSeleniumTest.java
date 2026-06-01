package com.restaurante.restaurantetesting.ui;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;
//arranca la app COMPLETA en un servidor real en un puerto aleatorio (no es MockMvc simulado). Selenium
//  necesita un servidor HTTP de verdad al que apuntar el navegador.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloSeleniumTest {

    @LocalServerPort
    int port; // puerto aleatorio, Spring te inyecta el puerto aleatorio donde arrancó la app.

    @Test
    void restaurantList() {
        WebDriver driver = new ChromeDriver(); //abre un Chrome real. Selenium Manager descarga el chromedriver compatible solo.
        driver.get("http://localhost:" + port + "/restaurants");// acción: el navegador navega a esa URL.
        driver.manage().window().maximize(); //maximiza la ventana.


        //busca el primer <h1> de la página y lee su texto.
        String tituloH1 = driver.findElement(By.tagName("h1")).getText();
        assertTrue(tituloH1.contains("Bienvenido a la lista de restaurantes")); //verifica que ese h1 contiene el saludo.
        driver.quit(); //cierra el navegador.
    }
}
