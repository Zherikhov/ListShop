package com.zherikhov.listshop.service.db;

import com.zherikhov.listshop.dao.ContactRepository;
import com.zherikhov.listshop.entity.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContactService {
    Logger logger = LoggerFactory.getLogger(ContactService.class);

    private final ContactRepository repository;

    public ContactService(ContactRepository repository) {
        this.repository = repository;
    }

    public void save(Contact contact) {
        logger.info("Contact " + contact.getNickName() + "has been saved");

        repository.save(contact);
    }

    public Contact findById(int id) {
        logger.info("Search for a contact by ID " + id);

        Contact subscriber = null;
        Optional<Contact> optionalSubscriber = repository.findById(id);

        if (optionalSubscriber.isPresent()) {
            subscriber = optionalSubscriber.get();
        }
        return subscriber;
    }
}
