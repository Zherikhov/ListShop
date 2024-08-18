package com.zherikhov.listshop;

import com.zherikhov.listshop.entity.Contact;
import com.zherikhov.listshop.entity.Item;
import com.zherikhov.listshop.entity.ListShop;
import com.zherikhov.listshop.entity.Subscriber;
import com.zherikhov.listshop.service.button.InlineKeyButtonService;
import com.zherikhov.listshop.service.commands.CommandService;
import com.zherikhov.listshop.service.db.ContactService;
import com.zherikhov.listshop.service.db.ItemService;
import com.zherikhov.listshop.service.db.ListShopService;
import com.zherikhov.listshop.service.db.SubscriberService;
import com.zherikhov.listshop.service.sender.SendMessageService;
import com.zherikhov.listshop.utils.CheckUtil;
import com.zherikhov.listshop.utils.ResourcesUtil;
import com.zherikhov.listshop.utils.TextFormatUtil;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.zherikhov.listshop.constants.TextMessage.*;

public class TelegramBotApplication extends TelegramLongPollingBot {
    Logger logger = LoggerFactory.getLogger(TelegramBotApplication.class);

    private final SubscriberService subscriberService;
    private final ContactService contactService;
    private final ListShopService listShopService;
    private final ItemService itemService;

    private final CommandService startCommand = new CommandService();
    private final SendMessageService sendMessageService = new SendMessageService();
    private final InlineKeyButtonService inlineKeyButtonService = new InlineKeyButtonService();

    private User user = null;
    Subscriber checkingSubscriber = null;

    public TelegramBotApplication(String botToken,
                                  SubscriberService service,
                                  ContactService contactService,
                                  ListShopService listShopService,
                                  ItemService itemService) {
        super(botToken);
        this.subscriberService = service;
        this.contactService = contactService;
        this.listShopService = listShopService;
        this.itemService = itemService;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        if (!update.hasCallbackQuery()) {
            user = update.getMessage().getFrom();
        }

        if (subscriberService.findById(user.getId()) == null) {
            subscriberService.save(new Subscriber(user.getId(), user.getUserName(), user.getFirstName(), user.getLastName()));
        }
        Subscriber subscriber = subscriberService.findById(user.getId());

        if (subscriber != null) {
            if ((update.hasMessage() && update.getMessage().hasText() || update.hasCallbackQuery()) && subscriber.getStepStatus() >= 300 && subscriber.getStepStatus() < 400) {
                makeList(update, subscriber);
                return;
            }

            if ((update.hasMessage() && update.getMessage().hasText() || update.hasCallbackQuery()) && subscriber.getStepStatus() >= 200 && subscriber.getStepStatus() < 300) {
                contacts(update, subscriber);
                return;
            }

            if (update.hasMessage() && update.getMessage().hasText() && subscriber.getStepStatus() >= 100 && subscriber.getStepStatus() < 200) {
                feedback(update, subscriber);
                return;
            }
        }

        String command = CheckUtil.checkCommand(update.getMessage());
        if (command != null) {
            switch (command) {
                case "/start" -> {
                    logger.info("/START " + user.getId() + " " + user.getUserName());

                    if (subscriberService.findById(user.getId()) == null) {
                        subscriberService.save(new Subscriber(user.getId(), user.getUserName(), user.getFirstName(), user.getLastName()));
                        logger.info("Added a new user to DB " + user.getId() + " " + user.getUserName());
                    }
                    execute(startCommand.start(update));
                }
                case "/help" -> {
                    logger.info("/HELP " + user.getUserName());
                    execute(sendMessageService.createMessage(update, "Your help"));
                }
            }
            return;
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            switch (update.getMessage().getText()) {
                case "My lists" -> {
                    logger.info("My lists " + user.getId() + " " + user.getUserName());

                    Objects.requireNonNull(subscriber).setStepStatus(300);
                    subscriberService.save(subscriber);
                    execute(inlineKeyButtonService.setInlineButtonAllButtonsWithShare(update, SELECT_LIST, getAllListShopNames(subscriber)));
                }
                case "Contacts" -> {
                    logger.info("Add a contact " + user.getId() + " " + user.getUserName());

                    Objects.requireNonNull(subscriber).setStepStatus(200);
                    subscriberService.save(subscriber);
                    execute(inlineKeyButtonService.setInlineButtonAllButtonsWithoutShare(update, MY_CONTACTS, getAllContacts(subscriber)));
                }
                case "Feedback" -> {
                    logger.info("Feedback " + user.getId() + " " + user.getUserName());

                    Objects.requireNonNull(subscriber).setStepStatus(100);
                    subscriberService.save(subscriber);
                    execute(sendMessageService.createMessage(update, WRITE_YOUR_FEEDBACK));
                }
                case "About Bot" -> {
                    logger.info("About Bot " + user.getId() + " " + user.getUserName());

                    execute(startCommand.start(update));
                }
            }
        }
    }

    @SneakyThrows
    public void makeList(Update update, Subscriber subscriber) {
        String data = update.hasCallbackQuery() ? update.getCallbackQuery().getData().split(":")[1] : null;
        String text = update.hasMessage() ? update.getMessage().getText() : null;

        ListShop activeListShop;
        List<String> names;


        if (data != null) {
            //просто отмена
            if (data.equals(CANCEL)) { // ✔️
                subscriber.setStepStatus(0);
                subscriber.setActiveList(0);
                subscriberService.save(subscriber);
                execute(sendMessageService.editInlineMessage(update, CANCELED));

            } else if (subscriber.getStepStatus() == 300) {
                //подготовка к удалению списка
                if (data.equals(DELETE_BUTTON)) {
                    subscriber.setStepStatus(331);
                    subscriberService.save(subscriber);

                    names = getAllListShopNames(subscriber);
                    execute(sendMessageService.editInlineMessage(update, DELETE_LIST));
                    execute(inlineKeyButtonService.setInlineButtonForDelete(update, "Choose carefully!", names));

                    //подготовка к созданию нового списка
                } else if (data.equals(CREATE_BUTTON)) {
                    subscriber.setStepStatus(311);
                    subscriberService.save(subscriber);
                    execute(sendMessageService.editInlineMessage(update, PRINT_NAME_LIST));

                    //выбор списка для отображения внутренних элементов
                } else {
                    subscriber.setActiveList(listShopService.findByNameAndSubscriberId(data, subscriber).getId());
                    subscriber.setStepStatus(341);
                    subscriberService.save(subscriber);

                    names = getAllItemNamesByActiveList(subscriber);
                    execute(sendMessageService.editInlineMessage(update, "Selected " + data)); //TODO: надо понять как удалять сообщение
                    execute(inlineKeyButtonService.setInlineButtonAllButtonsWithoutShare(update, data, names));
                }

                //подготовка к созданию элемента списка
            } else if (subscriber.getStepStatus() == 341 && data.equals(CREATE_BUTTON)) {
                subscriber.setStepStatus(312);
                subscriberService.save(subscriber);
                execute(sendMessageService.editInlineMessage(update, PRINT_NAME_ITEM));

                //подготовка к удалению элемента списка
            } else if (subscriber.getStepStatus() == 341 && data.equals(DELETE_BUTTON)) {
                subscriber.setStepStatus(332);
                subscriberService.save(subscriber);

                names = getAllItemNamesByActiveList(subscriber);
                execute(sendMessageService.editInlineMessage(update, DELETE_ITEM));
                execute(inlineKeyButtonService.setInlineButtonForDelete(update, "Choose carefully!", names));

                //удаление списка
            } else if (subscriber.getStepStatus() == 331) {
                ListShop listShop = listShopService.findByNameAndSubscriberId(data, subscriber);
                itemService.deleteByListShop(listShop);
                listShopService.deleteByNameAndSubscriber(data, subscriber);

                names = getAllListShopNames(subscriber);
                execute(sendMessageService.editInlineMessage(update, data + " has been deleted"));
                execute(inlineKeyButtonService.setInlineButtonForDelete(update, "Choose carefully!", names));

                //удаление элемента списка
            } else if (subscriber.getStepStatus() == 332) {
                ListShop listShop = listShopService.findById(subscriber.getActiveList());
                itemService.deleteByNameAndListShop(data, listShop);
                subscriber.setStepStatus(341);

                names = getAllItemNamesByActiveList(subscriber);
                execute(sendMessageService.editInlineMessage(update, data + " has been deleted"));
                execute(inlineKeyButtonService.setInlineButtonAllButtonsWithoutShare(update, data, names));
            }
        } else if (text != null) {
            //создание нового списка
            if (subscriber.getStepStatus() == 311) {
                listShopService.save(new ListShop(subscriber, text));

                subscriber.setStepStatus(300);
                subscriberService.save(subscriber);

                names = getAllListShopNames(subscriber);
                execute(inlineKeyButtonService.setInlineButtonAllButtonsWithoutShare(update, SELECT_LIST, names));

                //создание нового элемента списка
            } else if (subscriber.getStepStatus() == 312) {
                activeListShop = listShopService.findById(subscriber.getActiveList());
                itemService.save(new Item(activeListShop, update.getMessage().getText()));

                subscriber.setStepStatus(341);
                subscriberService.save(subscriber);

                names = getAllItemNamesByActiveList(subscriber);
                execute(inlineKeyButtonService.setInlineButtonAllButtonsWithoutShare(update, update.getMessage().getText(), names));
            }
        }
    }

    @SneakyThrows
    public void contacts(Update update, Subscriber subscriber) {
        String data = update.hasCallbackQuery() ? update.getCallbackQuery().getData().split(":")[1] : null;
        String text = update.hasMessage() ? update.getMessage().getText() : null;

        int stepStatus = subscriber.getStepStatus();
        List<String> names;

        if (data != null) {
            switch (data) {
                case CANCEL -> {
                    subscriber.setStepStatus(0);
                    subscriberService.save(subscriber);
                    execute(sendMessageService.editInlineMessage(update, CANCELED));
                }
                case CREATE_BUTTON -> {
                    subscriber.setStepStatus(211);
                    subscriberService.save(subscriber);
                    execute(sendMessageService.editInlineMessage(update, PRINT_YOUR_CONTACT));
                }
                case EDIT_BUTTON -> {
                    subscriber.setStepStatus(221);
                    subscriberService.save(subscriber);
                    execute(sendMessageService.createMessage(update, "crutch"));
                }
                case DELETE_BUTTON -> {
                    subscriber.setStepStatus(231);
                    subscriberService.save(subscriber);
                    execute(sendMessageService.editInlineMessage(update, DELETE_CONTACT));

                    names = getAllContacts(subscriber);
                    execute(inlineKeyButtonService.setInlineButtonForDelete(update, "Choose carefully!", names));
                }
                default -> {
                    if (stepStatus == 231) {
                        contactService.deleteBySubscriberAndNickName(subscriber, data);
                        execute(sendMessageService.editInlineMessage(update, "Done!"));

                        subscriber.setStepStatus(0);
                        subscriberService.save(subscriber);
                    }
                }
            }

        } else if (text != null) {
            String contactUserName = TextFormatUtil.userNameFormat(update.getMessage().getText());

            if (stepStatus == 211) {
                checkingSubscriber = subscriberService.findByUserName(contactUserName);
                if (checkingSubscriber != null) {
                    subscriber.setStepStatus(212);
                    subscriberService.save(subscriber);
                    execute(sendMessageService.createMessage(update, PRINT_NICKNAME));
                } else {
                    subscriber.setStepStatus(0);
                    subscriberService.save(subscriber);
                    execute(sendMessageService.createMessage(update, NOT_FOUND_NICKNAME));
                }
            } else if (stepStatus == 212) {
                Contact contact = new Contact(subscriber, text, checkingSubscriber.getUserName());
                contactService.save(contact);

                subscriber.setStepStatus(0);
                subscriberService.save(subscriber);

                execute(sendMessageService.createMessage(update, ADDED_IN_YOUR_CONTACT_LIST));
            }
        }
    }

    private List<String> getAllItemNamesByActiveList(Subscriber subscriber) {
        ListShop activeListShop = listShopService.findById(subscriber.getActiveList());
        List<Item> allByIdListShop = activeListShop != null ? itemService.findAllByIdListShop(activeListShop) : null;
        return allByIdListShop != null ? allByIdListShop.stream().map(Item::getName).toList() : new ArrayList<>();
    }

    private List<String> getAllListShopNames(Subscriber subscriber) {
        List<String> listShopNames = new ArrayList<>();
        List<ListShop> listShops = listShopService.findAllByIdSubscriber(subscriber);
        for (ListShop i : listShops) {
            listShopNames.add(i.getName());
        }
        return listShopNames;
    }

    private List<String> getAllContacts(Subscriber subscriber) {
        List<String> listShopNames = new ArrayList<>();
        List<Contact> listShops = contactService.getAllBySubscriber(subscriber);
        for (Contact i : listShops) {
            listShopNames.add(i.getNickName());
        }
        return listShopNames;
    }

    @SneakyThrows
    public void feedback(Update update, Subscriber subscriber) {
        execute(sendMessageService.createMessageForSupport(update.getMessage().getText()));

        subscriber.setStepStatus(0);
        subscriberService.save(subscriber);
        execute(sendMessageService.createMessage(update, WILL_CONTACT_YOU));
    }

    @Override
    public String getBotUsername() {
        return ResourcesUtil.getProperties("application.secure.properties", "telegram.name");
    }
}
