package com.zherikhov.listshop.dao;

import com.zherikhov.listshop.entity.Contact;
import com.zherikhov.listshop.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
    List<Contact> findAllBySubscriber(Subscriber subscriber);
    void deleteBySubscriberAndNickName(Subscriber subscriber, String nickName);
}
