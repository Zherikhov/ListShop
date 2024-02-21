package com.zherikhov.listshop;

import com.zherikhov.listshop.constants.text.Messages;
import com.zherikhov.listshop.entity.Contact;
import com.zherikhov.listshop.entity.Item;
import com.zherikhov.listshop.entity.ListShop;
import com.zherikhov.listshop.entity.Subscriber;
import com.zherikhov.listshop.service.button.InlineKeyButtonService;
import com.zherikhov.listshop.service.commands.Commands;
import com.zherikhov.listshop.service.db.ContactService;
import com.zherikhov.listshop.service.db.ItemService;
import com.zherikhov.listshop.service.db.ListShopService;
import com.zherikhov.listshop.service.db.SubscriberService;
import com.zherikhov.listshop.service.sender.SendMessageService;
import com.zherikhov.listshop.utils.Check;
import com.zherikhov.listshop.utils.Resources;
import com.zherikhov.listshop.utils.TextFormat;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class TelegramBotApplication extends TelegramLongPollingBot {
    private final Commands startCommand = new Commands();

    private final SubscriberService subscriberService;
    private final ContactService contactService;
    private final ListShopService listShopService;
    private final ItemService itemService;

    private final SendMessageService sendMessageService = new SendMessageService();
    private final InlineKeyButtonService inlineKeyButtonService = new InlineKeyButtonService();

    User user = null;

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
            if ((update.hasMessage() && update.getMessage().hasText() || update.hasCallbackQuery()) && subscriber.getStepStatus() > 29 && subscriber.getStepStatus() < 40) {
                makeList(update, subscriber);
            }

            if (update.hasMessage() && update.getMessage().hasText() && subscriber.getStepStatus() > 19 && subscriber.getStepStatus() < 30) {
                addContact(update, subscriber);
            }

            if (update.hasMessage() && update.getMessage().hasText() && subscriber.getStepStatus() > 9 && subscriber.getStepStatus() < 20) {
                feedback(update, subscriber);
            }
        }

        String command = Check.checkCommand(update.getMessage());
        if (command != null) {
            switch (command) {
                case "/start" -> {
                    log.info("/start -> " + user.getUserName());

                    if (subscriberService.findById(user.getId()) == null) {
                        subscriberService.save(new Subscriber(user.getId(), user.getUserName(), user.getFirstName(), user.getLastName()));
                    }
                    execute(startCommand.start(update));
                }
                case "/help" -> {
                    log.info("/help -> " + user.getUserName());
                    execute(sendMessageService.createMessage(update, "Your help"));
                }
            }
            return;
        }

        /*
          reserved statuses
          0 - beginner
          10 - 19 - Feedback
          20 - 29 - Add a contact
          30 - 39 - Make a list
         */
        if (update.hasMessage() && update.getMessage().hasText()) {
            switch (update.getMessage().getText()) {
                case "Make a list" -> {
                    log.info("Make a list - " + user.getUserName());

                    List<String> listShopNames = new ArrayList<>();
                    List<ListShop> listShops = listShopService.findAllByIdSubscriber(subscriber);
                    for (ListShop i : listShops) {
                        listShopNames.add(i.getName());
                    }

                    Objects.requireNonNull(subscriber).setStepStatus(30);
                    subscriberService.save(subscriber);
                    execute(inlineKeyButtonService.setInlineButton(update, Messages.MAKE_A_LIST_STEP1, listShopNames));
                }
                case "Add a contact" -> {
                    log.info("Add a contact - " + user.getUserName());

                    Objects.requireNonNull(subscriber).setStepStatus(20);
                    subscriberService.save(subscriber);
                    execute(sendMessageService.createMessage(update, Messages.ADD_A_CONTACT_STEP1));
                }
                case "Feedback" -> {
                    log.info("Feedback - " + user.getUserName());

                    Objects.requireNonNull(subscriber).setStepStatus(10);
                    subscriberService.save(subscriber);
                    execute(sendMessageService.createMessage(update, Messages.FEEDBACK_STEP1));
                }
                case "About Bot" -> {
                    log.info("About Bot - " + user.getUserName());

                    execute(startCommand.start(update));
                }
            }
        }
    }

    @SneakyThrows
    public void makeList(Update update, Subscriber subscriber) {
        String data = update.hasCallbackQuery() ? update.getCallbackQuery().getData().split(":")[1] : null;
        List<String> names;

        if (data != null) {
            if (data.equals("Cancel")) {
                subscriber.setStepStatus(0);
                subscriberService.save(subscriber);
                execute(sendMessageService.editInlineMessage(update, "Canceled"));

            } else if (subscriber.getStepStatus() == 30 && !data.equals("New")) {
                ListShop listShop = listShopService.findByName(data);
                List<Item> allByIdListShop = itemService.findAllByIdListShop(listShop);
                names = allByIdListShop.stream().map(Item::getName).toList();
                subscriber.setActiveList(listShopService.findByNameAndSubscriberId(data, subscriber).getId());
                subscriberService.save(subscriber);
                execute(sendMessageService.editInlineMessage(update, "was " + data)); //TODO: надо понять как удалять сообщение
                execute(inlineKeyButtonService.setInlineButton(update, data, names));

                subscriber.setStepStatus(32);
                subscriberService.save(subscriber);
            } else if (subscriber.getStepStatus() == 30 && data.equals("New")) {
                execute(sendMessageService.editInlineMessage(update, "Please enter a name list"));
                subscriber.setStepStatus(31);
                subscriberService.save(subscriber);
            } else if (subscriber.getStepStatus() == 32 && data.equals("New")) {
                execute(sendMessageService.editInlineMessage(update, "Please enter a name item"));
                subscriber.setStepStatus(33);
                subscriberService.save(subscriber);
            }
        } else if (subscriber.getStepStatus() == 31 && update.hasMessage()) {
            listShopService.save(new ListShop(subscriber, update.getMessage().getText()));
            subscriber.setStepStatus(0);
            subscriberService.save(subscriber);
            execute(sendMessageService.createMessage(update, "Done!"));
        } else if (subscriber.getStepStatus() == 33 && update.hasMessage()) {
            ListShop listShop = listShopService.findById(subscriber.getActiveList());
            itemService.save(new Item(listShop, update.getMessage().getText()));
            subscriber.setStepStatus(0);
            subscriberService.save(subscriber);
            execute(sendMessageService.createMessage(update, "Done!"));
        }
    }

    @SneakyThrows
    public void addContact(Update update, Subscriber subscriber) {
        if (subscriber.getStepStatus() == 20) {
            String userName = TextFormat.userNameFormat(update.getMessage().getText());
            Subscriber checkingSubscriber = subscriberService.findByUserName(userName);

            if (checkingSubscriber != null) {
                subscriber.setStepStatus(21);
                subscriberService.save(subscriber);
                execute(sendMessageService.createMessage(update, Messages.ADD_A_CONTACT_STEP2));
            } else {
                subscriber.setStepStatus(0);
                subscriberService.save(subscriber);
                execute(sendMessageService.createMessage(update, Messages.ADD_A_CONTACT_NOT_FOUND_NICKNAME));
            }
        } else if (subscriber.getStepStatus() == 21) {
            Contact contact = new Contact(subscriber, update.getMessage().getText(), subscriber.getUserName());
            contactService.save(contact);

            subscriber.setStepStatus(0);
            subscriberService.save(subscriber);

            execute(sendMessageService.createMessage(update, Messages.ADD_A_CONTACT_STEP3));
        }
    }

    @SneakyThrows
    public void feedback(Update update, Subscriber subscriber) {
        execute(sendMessageService.createMessageForSupport(update.getMessage().getText()));

        subscriber.setStepStatus(0);
        subscriberService.save(subscriber);
        execute(sendMessageService.createMessage(update, Messages.FEEDBACK_STEP2));
    }

    @Override
    public String getBotUsername() {
        return Resources.getProperties("application.secure.properties", "telegram.name");
    }
}
