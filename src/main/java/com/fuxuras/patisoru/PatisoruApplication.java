package com.fuxuras.patisoru;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class PatisoruApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatisoruApplication.class, args);
    }

}
