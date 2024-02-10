package com.zherikhov.listshop;

import com.zherikhov.listshop.service.db.ContactService;
import com.zherikhov.listshop.service.db.SubscriberService;
import com.zherikhov.listshop.utils.Resources;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class App {

    private final SubscriberService subscriberService;
    private final ContactService contactService;

    public App(SubscriberService service, ContactService contactService) {
        this.subscriberService = service;
        this.contactService = contactService;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @PostConstruct
    public void postConstruct() {
        TelegramBotApplication telegramBotApplication = new TelegramBotApplication(Resources.getProperties("application.secure.properties", "telegtam.token"), subscriberService, contactService);
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramBotApplication);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
