package com.zherikhov.listshop.service.db;

import com.zherikhov.listshop.dao.SubscriberRepository;
import com.zherikhov.listshop.entity.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubscriberService {
    Logger logger = LoggerFactory.getLogger(SubscriberService.class);

    private final SubscriberRepository repository;

    public SubscriberService(SubscriberRepository subscriberRepository) {
        this.repository = subscriberRepository;
    }

    public void save(Subscriber subscriber) {
        logger.info("Subscriber " + subscriber.getId() + "has been saved");

        repository.save(subscriber);
    }

    public Subscriber findById(long id) {
        logger.info("Search for subscriber by ID " + id);

        Subscriber subscriber = null;
        Optional<Subscriber> optionalSubscriber = repository.findById(id);

        if (optionalSubscriber.isPresent()) {
            subscriber = optionalSubscriber.get();
        }
        return subscriber;
    }

    public Subscriber findByUserName(String userName) {
        logger.info("Search for subscriber by UserName " + userName);

        Subscriber subscriber = null;
        Optional<Subscriber> optionalSubscriber = repository.findByUserName(userName);

        if (optionalSubscriber.isPresent()) {
            subscriber = optionalSubscriber.get();
        }
        return subscriber;
    }
}
