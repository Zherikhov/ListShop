package com.zherikhov.listshop.service.db;

import com.zherikhov.listshop.dao.ItemRepository;
import com.zherikhov.listshop.entity.Item;
import com.zherikhov.listshop.entity.ListShop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    Logger logger = LoggerFactory.getLogger(ItemService.class);

    private final ItemRepository repository;

    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    public void save(Item item) {
        logger.info("Item " + item.getName() + "has been saved");

        repository.save(item);
    }

    public List<Item> findAllByIdListShop(ListShop listShop) {
        logger.info("Search for items by ID ListSHop " + listShop.getId());

        return repository.findAllByListShop(listShop);
    }

    public void deleteByNameAndListShop(String name, ListShop listShop) {
        logger.info(name + " was been deleted from " + listShop.getId());

        repository.deleteByNameAndListShop(name, listShop);
    }

}
