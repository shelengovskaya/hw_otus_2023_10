package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/clients

*/
@SpringBootApplication
public class WebServer {
    public static void main(String[] args) {
        SpringApplication.run(WebServer.class, args);
    }
}
