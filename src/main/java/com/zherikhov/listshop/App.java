package com.zherikhov.listshop;

import com.zherikhov.listshop.service.SubscriberService;
import com.zherikhov.listshop.utils.Resources;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class App {

    public App(SubscriberService service) {
        this.service = service;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    private final SubscriberService service;

    @PostConstruct
    public void postConstruct() {
        TelegramBotApplication telegramBotApplication = new TelegramBotApplication(Resources.getProperties("application.secure.properties", "telegtam.token"), service);
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramBotApplication);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
