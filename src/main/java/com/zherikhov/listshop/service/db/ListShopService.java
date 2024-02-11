package com.zherikhov.listshop.service.db;

import com.zherikhov.listshop.dao.ListShopRepository;
import com.zherikhov.listshop.entity.ListShop;
import com.zherikhov.listshop.entity.Subscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ListShopService {

    private final ListShopRepository repository;

    public ListShopService(ListShopRepository repository) {
        this.repository = repository;
    }

    public List<ListShop> findAllByIdSubscriber(Subscriber subscriber) {
        return repository.findAllBySubscriber(subscriber);
    }

    public void save(ListShop listShop) {
        repository.save(listShop);
    }

    public ListShop findById(int id) {
        ListShop listShop = null;
        Optional<ListShop> optionalSubscriber = repository.findById(id);

        if (optionalSubscriber.isPresent()) {
            listShop = optionalSubscriber.get();
        }
        return listShop;
    }

    public ListShop findByUserName(String userName) {
        ListShop listShop = null;
        Optional<ListShop> optionalSubscriber = repository.findByName(userName);

        if (optionalSubscriber.isPresent()) {
            listShop = optionalSubscriber.get();
        }
        return listShop;
    }
}
