package com.zherikhov.listshop.service.db;

import com.zherikhov.listshop.dao.ItemRepository;
import com.zherikhov.listshop.entity.Item;
import com.zherikhov.listshop.entity.ListShop;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemService {
    private final ItemRepository repository;

    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    public void save(Item item) {
        repository.save(item);
    }

    public List<Item> findAllByIdListShop(ListShop listShop) {
        return repository.findAllByListShop(listShop);
    }

    public void deleteByNameAndListShop(String name, ListShop listShop) {
        repository.deleteByNameAndListShop(name, listShop);
    }

    @Transactional
    public void deleteByListShop(ListShop listShop) {
        repository.deleteAllByListShop(listShop);
    }

}
