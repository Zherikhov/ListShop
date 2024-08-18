package com.zherikhov.listshop.dao;

import com.zherikhov.listshop.entity.Item;
import com.zherikhov.listshop.entity.ListShop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    Optional<Item> findByName(String name);
    List<Item> findAllByListShop(ListShop listShop);
    void deleteByNameAndListShop(String name, ListShop listShop);
    void deleteAllByListShop(ListShop listShop);
}
