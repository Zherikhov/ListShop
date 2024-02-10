package com.zherikhov.listshop.service.db;

import com.zherikhov.listshop.dao.ContactRepository;
import com.zherikhov.listshop.entity.Contact;
import com.zherikhov.listshop.entity.Subscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ContactService {

    private final ContactRepository repository;

    public ContactService(ContactRepository repository) {
        this.repository = repository;
    }

    public List<Contact> findAll() {
        List<Contact> contacts = repository.findAll();
        log.info(contacts.toString());
        return contacts;
    }

    public void save(Contact contact) {
        repository.save(contact);
        log.info("Added a new contact to DB: " + contact.getId() + " " +
                " " + contact.getNickName() + " " + contact.getUserName() + " from user - " + contact.getSubscriber().getId());
    }

    public Contact findById(int id) {
        Contact subscriber = null;
        Optional<Contact> optionalSubscriber = repository.findById(id);

        if (optionalSubscriber.isPresent()) {
            subscriber = optionalSubscriber.get();
            log.info(subscriber.toString());
        }
        return subscriber;
    }
}
