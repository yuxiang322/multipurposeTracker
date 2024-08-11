package com.xiang.multipurposeTracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    //test decryption and connection
    @Bean
    public CommandLineRunner testDatabaseConnection(JdbcTemplate jdbcTemplate) {
        return args -> {
            try {

                List<String> emails = jdbcTemplate.queryForList("SELECT Email FROM UserDetails", String.class);

                for (String email : emails) {
                    System.out.println("Email: " + email);
                }

                System.out.println("Database connection successful!");
            } catch (Exception e) {
                System.err.println("Database connection failed: " + e.getMessage());
            }
        };
    }
}

