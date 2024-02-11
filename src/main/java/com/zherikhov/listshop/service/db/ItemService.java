package com.zherikhov.listshop.service.db;

import com.zherikhov.listshop.dao.ItemRepository;
import com.zherikhov.listshop.entity.Item;
import com.zherikhov.listshop.entity.ListShop;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ItemService {

    private final ItemRepository repository;

    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    public List<Item> findAllByIdListShop(ListShop listShop) {
        return repository.findAllByListShop(listShop);
    }
}
