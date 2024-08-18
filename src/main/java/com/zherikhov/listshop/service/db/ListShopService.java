package com.zherikhov.listshop.service.db;

import com.zherikhov.listshop.dao.ListShopRepository;
import com.zherikhov.listshop.entity.ListShop;
import com.zherikhov.listshop.entity.Subscriber;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ListShopService {
    private final ListShopRepository repository;

    public ListShopService(ListShopRepository repository) {
        this.repository = repository;
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

    public List<ListShop> findAllByIdSubscriber(Subscriber subscriber) {
        return repository.findAllBySubscriber(subscriber);
    }

    public ListShop findByNameAndSubscriberId(String name, Subscriber subscriber) {
        ListShop listShop = null;
        Optional<ListShop> optionalListShop = repository.findByNameAndSubscriber(name, subscriber);

        if (optionalListShop.isPresent()) {
            listShop = optionalListShop.get();
        }
        return listShop;
    }

    @Transactional
    public void deleteByNameAndSubscriber(String name, Subscriber subscriber) {
        repository.deleteByNameAndSubscriber(name, subscriber);
    }
}
