package com.zherikhov.listshop.dao;

import com.zherikhov.listshop.entity.Subscriber;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubscriberRepository extends CrudRepository<Subscriber, Long> {

    List<Subscriber> findByUserName(String userName);

    Subscriber findById(long id);
}
