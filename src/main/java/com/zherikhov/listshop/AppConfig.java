package com.zherikhov.listshop;

import com.zherikhov.listshop.service.db.ContactService;
import com.zherikhov.listshop.service.db.ItemService;
import com.zherikhov.listshop.service.db.ListShopService;
import com.zherikhov.listshop.service.db.SubscriberService;
import com.zherikhov.listshop.utils.DataBaseUtil;
import com.zherikhov.listshop.utils.ResourcesUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public TelegramBotApplication getTelegram(SubscriberService subscriberService, ContactService contactService, ListShopService listShopService, ItemService itemService) {
        DataBaseUtil.resetSubscribersAttributes(subscriberService);

        return new TelegramBotApplication(ResourcesUtil.getProperties("application.secure.properties", "telegtam.token"),
                subscriberService,
                contactService,
                listShopService,
                itemService);
    }
}
