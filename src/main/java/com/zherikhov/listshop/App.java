package com.zherikhov.listshop;

import com.zherikhov.listshop.service.db.ContactService;
import com.zherikhov.listshop.service.db.ItemService;
import com.zherikhov.listshop.service.db.ListShopService;
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
    private final ListShopService listShopService;
    private final ItemService itemService;

    public App(SubscriberService service, ContactService contactService, ListShopService listShopService, ItemService itemService) {
        this.subscriberService = service;
        this.contactService = contactService;
        this.listShopService = listShopService;
        this.itemService = itemService;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @PostConstruct
    public void postConstruct() {
        TelegramBotApplication telegramBotApplication = new TelegramBotApplication(
                Resources.getProperties("application.secure.properties", "telegtam.token"),
                subscriberService,
                contactService,
                listShopService,
                itemService);
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramBotApplication);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
