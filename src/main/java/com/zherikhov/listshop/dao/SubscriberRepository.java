package com.zherikhov.listshop.dao;

import com.zherikhov.listshop.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {
    Optional<Subscriber> findByUserName(String userName);
}
