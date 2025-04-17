package com.example.remitlyintern;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class RemitlyInternApplication {

    public static void main(String[] args) {
        SpringApplication.run(RemitlyInternApplication.class, args);
    }

}
