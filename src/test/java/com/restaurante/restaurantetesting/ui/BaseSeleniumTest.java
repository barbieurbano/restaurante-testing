package com.restaurante.restaurantetesting.ui;

import com.restaurante.restaurantetesting.model.Dish;
import com.restaurante.restaurantetesting.model.Restaurant;
import com.restaurante.restaurantetesting.model.Review;
import com.restaurante.restaurantetesting.model.User;
import com.restaurante.restaurantetesting.model.enums.DishType;
import com.restaurante.restaurantetesting.model.enums.Role;
import com.restaurante.restaurantetesting.repository.DishRepository;
import com.restaurante.restaurantetesting.repository.RestaurantRepository;
import com.restaurante.restaurantetesting.repository.ReviewRepository;
import com.restaurante.restaurantetesting.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseSeleniumTest {

    @LocalServerPort
    int port;
    @Autowired
    RestaurantRepository restaurantRepo;
    @Autowired
    DishRepository dishRepo;
    @Autowired
    ReviewRepository reviewRepo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    PasswordEncoder passwordEncoder;

    String baseUrl;
    WebDriver driver;
    WebDriverWait wait;

    Restaurant pizzeria; // con platos
    Restaurant taberna;  // sin platos
    Dish pizza;
    Dish tiramisu;
    Review pizzeriaOK; //es del restaurante no del plato
    Review pizzeriaMal; //es del restaurante no del plato
    User user, admin;

    @BeforeEach
    void setUp() {
        // crear datos demo
        reviewRepo.deleteAll();
        dishRepo.deleteAll();
        restaurantRepo.deleteAll();
        userRepo.deleteAll();

        user = userRepo.save(User.builder()
                .username("user").email("user@gmail.com").password(passwordEncoder.encode("user")).role(Role.ROLE_USER)
                .build());
        admin = userRepo.save(User.builder()
                .username("admin").email("admin@gmail.com").password(passwordEncoder.encode("admin")).role(Role.ROLE_ADMIN)
                .build());

        pizzeria = restaurantRepo.save(Restaurant.builder().name("Pizzeria Luigi").averagePrice(10.0).active(true).description("Masa artesanal").build());
        taberna = restaurantRepo.save(Restaurant.builder().name("Taberna").averagePrice(20.0).active(false).build());
        pizza = dishRepo.save(Dish.builder().name("Pizza 4 Quesos").price(12d).type(DishType.MAIN_COURSE).description("pizza bien").restaurant(pizzeria).build());
        tiramisu = dishRepo.save(Dish.builder().name("Tiramisú Café").price(3d).type(DishType.DESSERT).description("fetén").restaurant(pizzeria).build());
        pizzeriaOK = reviewRepo.save(Review.builder().title("Pectacular").rating(5).restaurant(pizzeria).content("Asombroso").build());
        pizzeriaMal = reviewRepo.save(Review.builder().title("Fatal").rating(1).dish(pizza).restaurant(pizzeria).content("Nada bien").creationDate(LocalDateTime.now().minusDays(1)).build());

                // inicializar y configurar driver
                baseUrl = "http://localhost:" + port + "/";
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofMillis(30L));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    void loginAdmin(){
        login("admin", "admin");
    }

    void loginUser(){
        login("user", "user");
    }

    void login(String username, String password){

    }
}
