package com.zherikhov.listshop.service.db;

import com.zherikhov.listshop.dao.ListShopRepository;
import com.zherikhov.listshop.entity.ListShop;
import com.zherikhov.listshop.entity.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ListShopService {
    Logger logger = LoggerFactory.getLogger(ListShopService.class);

    private final ListShopRepository repository;

    public ListShopService(ListShopRepository repository) {
        this.repository = repository;
    }

    public void save(ListShop listShop) {
        logger.info("ListShop " + listShop.getName() + "has been saved");

        repository.save(listShop);
    }

    public ListShop findById(int id) {
        logger.info("Search for listShop by ID " + id);

        ListShop listShop = null;
        Optional<ListShop> optionalSubscriber = repository.findById(id);

        if (optionalSubscriber.isPresent()) {
            listShop = optionalSubscriber.get();
        }
        return listShop;
    }

    public List<ListShop> findAllByIdSubscriber(Subscriber subscriber) {
        logger.info("Search for listShop by ID subscriber " + subscriber.getId());

        return repository.findAllBySubscriber(subscriber);
    }

    public ListShop findByName(String name) {
        logger.info("Search for listShop by name " + name);

        ListShop listShop = null;
        Optional<ListShop> optionalListShop = repository.findByName(name);

        if (optionalListShop.isPresent()) {
            listShop = optionalListShop.get();
        }
        return listShop;
    }

    public ListShop findByNameAndSubscriberId(String name, Subscriber subscriber) {
        logger.info("Search for listShop by NAME and ID subscriber " + name + "," + subscriber.getId());

        ListShop listShop = null;
        Optional<ListShop> optionalListShop = repository.findByNameAndSubscriber(name, subscriber);

        if (optionalListShop.isPresent()) {
            listShop = optionalListShop.get();
        }
        return listShop;
    }
}
