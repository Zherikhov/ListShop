package com.zherikhov.listshop;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class App {

    private final TelegramBotApplication telegramBotApplication;

    public App(TelegramBotApplication telegramBotApplication) {
        this.telegramBotApplication = telegramBotApplication;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);

    }

    @PostConstruct
    public void postConstruct() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this.telegramBotApplication);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
