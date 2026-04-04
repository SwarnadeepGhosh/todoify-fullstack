package com.swarna.todoFullStack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TodoFullStackApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoFullStackApplication.class, args);
    }

}
