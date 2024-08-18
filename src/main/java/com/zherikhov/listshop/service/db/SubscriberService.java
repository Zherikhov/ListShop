package com.zherikhov.listshop.service.db;

import com.zherikhov.listshop.dao.SubscriberRepository;
import com.zherikhov.listshop.entity.Subscriber;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriberService {

    private final SubscriberRepository repository;

    public SubscriberService(SubscriberRepository subscriberRepository) {
        this.repository = subscriberRepository;
    }

    public void save(Subscriber subscriber) {
        repository.save(subscriber);
    }

    public Subscriber findById(long id) {
        Optional<Subscriber> optionalSubscriber = repository.findById(id);
        return optionalSubscriber.orElse(null);
    }

    public Subscriber findByUserName(String userName) {
        Optional<Subscriber> optionalSubscriber = repository.findByUserName(userName);
        return optionalSubscriber.orElse(null);
    }

    public List<Subscriber> findAll() {
        return repository.findAll();
    }
}
