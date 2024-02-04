package com.zherikhov.listshop.service;

import com.zherikhov.listshop.dao.SubscriberRepository;
import com.zherikhov.listshop.entity.Subscriber;
import org.springframework.stereotype.Service;

@Service
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;

    public SubscriberService(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    public void save(Subscriber subscriber) {
        subscriberRepository.save(subscriber);
    }
}
