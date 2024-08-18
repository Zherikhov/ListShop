package com.zherikhov.listshop.service.db;

import com.zherikhov.listshop.dao.ContactRepository;
import com.zherikhov.listshop.entity.Contact;
import com.zherikhov.listshop.entity.Subscriber;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ContactService {

    private final ContactRepository repository;

    public ContactService(ContactRepository repository) {
        this.repository = repository;
    }

    public void save(Contact contact) {
        repository.save(contact);
    }

    public List<Contact> getAllBySubscriber(Subscriber subscriber) {
        return repository.findAllBySubscriber(subscriber);
    }

    public void deleteBySubscriberAndNickName(Subscriber subscriber, String nickName) {
        repository.deleteBySubscriberAndNickName(subscriber, nickName);
    }
}
