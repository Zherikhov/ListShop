package com.zherikhov.listshop;

import com.zherikhov.listshop.service.db.ContactService;
import com.zherikhov.listshop.service.db.ItemService;
import com.zherikhov.listshop.service.db.ListShopService;
import com.zherikhov.listshop.service.db.SubscriberService;
import com.zherikhov.listshop.utils.Resources;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public TelegramBotApplication getTelegram(SubscriberService subscriberService, ContactService contactService, ListShopService listShopService, ItemService itemService) {
        return new TelegramBotApplication(Resources.getProperties("application.secure.properties", "telegtam.token"),
                subscriberService,
                contactService,
                listShopService,
                itemService);
    }
}
