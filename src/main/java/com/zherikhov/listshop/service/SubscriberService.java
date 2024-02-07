package com.zherikhov.listshop.service;

import com.zherikhov.listshop.dao.SubscriberRepository;
import com.zherikhov.listshop.entity.Subscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SubscriberService {

    private final SubscriberRepository repository;

    public SubscriberService(SubscriberRepository subscriberRepository) {
        this.repository = subscriberRepository;
    }

    public List<Subscriber> findAll() {
        List<Subscriber> subscribers = repository.findAll();
        log.info(subscribers.toString());
        return subscribers;
    }

    public void save(Subscriber subscriber) {
        repository.save(subscriber);
        log.info("Added a new subscriber to DB: " + subscriber.getId() + " " + subscriber.getUserName() +
                " " + subscriber.getFirstName() + " " + subscriber.getLastName());
    }

    public Subscriber findById(long id) {
        Subscriber subscriber = null;
        Optional<Subscriber> optionalSubscriber = repository.findById(id);

        if (optionalSubscriber.isPresent()) {
            subscriber = optionalSubscriber.get();
            log.info(subscriber.toString());
        }
        return subscriber;
    }

    public Subscriber findByUserName(String userName) {
        Subscriber subscriber = null;
        Optional<Subscriber> optionalSubscriber = repository.findByUserName(userName);

        if (optionalSubscriber.isPresent()) {
            subscriber = optionalSubscriber.get();
            log.info(subscriber.toString());
        }
        return subscriber;
    }
}
