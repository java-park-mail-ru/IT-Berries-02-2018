package com.itberries2018.demo;

import com.itberries2018.demo.websockets.GameWebSocketHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;

@SpringBootApplication
public class Main {

    @Bean
    public WebSocketHandler webSocketHandler() {
        return new PerConnectionWebSocketHandler(GameWebSocketHandler.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}