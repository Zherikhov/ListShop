package com.zherikhov.listshop.service.db;

import com.zherikhov.listshop.dao.ListShopRepository;
import com.zherikhov.listshop.entity.ListShop;
import com.zherikhov.listshop.entity.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public ListShop findByNameAndSubscriberId(String name, Subscriber subscriber) {
        logger.info("Search for listShop by NAME and ID subscriber " + name + "," + subscriber.getId());

        ListShop listShop = null;
        Optional<ListShop> optionalListShop = repository.findByNameAndSubscriber(name, subscriber);

        if (optionalListShop.isPresent()) {
            listShop = optionalListShop.get();
        }
        return listShop;
    }

    @Transactional
    public void deleteByNameAndSubscriber(String name, Subscriber subscriber) {
        logger.info(name + " was been deleted from " + subscriber);

        repository.deleteByNameAndSubscriber(name, subscriber);
    }
}
