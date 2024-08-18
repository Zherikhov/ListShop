package com.zherikhov.listshop.utils;

import com.zherikhov.listshop.service.db.SubscriberService;

public class DataBaseUtil {
    public static void resetSubscribersAttributes(SubscriberService subscriberService) {
        subscriberService.findAll().forEach(element -> {
            element.setStepStatus(0);
            element.setActiveList(0);
            subscriberService.save(element);
        });
    }
}
