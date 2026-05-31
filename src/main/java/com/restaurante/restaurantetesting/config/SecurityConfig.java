package com.restaurante.restaurantetesting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    //passwordEncoder @Bean para cifrar y verificar contrasenias
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); //que no tenga el 4j
    }

    //SecutiryFilterChain @Bean Para proteger rutas
    //Define qué rutas son públicas y cuáles piden rol/login
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //permitir la consola H2 (deshabilitar CSRF y frames solo para esa ruta)
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        //Algunas rutas sera solo para ADMIN, otras estaran publicas (login, registro, css, bootstrap), privada (formulario de crear un restaurante)
        //Reglas de autorización por ruta
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/register", "/login", "/css/**", "/webjars/**", "/images/**").permitAll()

                .requestMatchers(HttpMethod.GET, "/restaurants/deactivate/*").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/restaurants/new").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/restaurants/edit/*").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/restaurants").permitAll()
                .requestMatchers(HttpMethod.GET, "/restaurants/*").permitAll()
                .requestMatchers(HttpMethod.POST, "/restaurants").hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST, "/dishes").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/dishes/new").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/dishes/edit/*").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/dishes").permitAll()
                .requestMatchers(HttpMethod.GET, "/dishes/*").permitAll()

                .requestMatchers(HttpMethod.POST, "/reviews").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.GET, "/reviews/new").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.GET, "/reviews/delete/*").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/reviews").permitAll()
                .requestMatchers(HttpMethod.GET, "/reviews/*").permitAll()

                .requestMatchers("/orders", "/orders/**").authenticated()

                .anyRequest().authenticated()// Lo demás: pide login
        );

        // Formulario de login
        http.formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/restaurants", true)
                .permitAll()
        );

        return http.build();

    }



}
