package com.zherikhov.listshop.dao;

import com.zherikhov.listshop.entity.ListShop;
import com.zherikhov.listshop.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ListShopRepository extends JpaRepository<ListShop, Integer> {
    Optional<ListShop> findByName(String name);
    List<ListShop> findAllBySubscriber(Subscriber subscriber);
}