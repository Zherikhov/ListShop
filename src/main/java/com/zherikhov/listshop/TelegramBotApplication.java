package com.zherikhov.listshop;

import com.zherikhov.listshop.commands.Commands;
import com.zherikhov.listshop.commands.SendMessageController;
import com.zherikhov.listshop.constants.text.Messages;
import com.zherikhov.listshop.entity.Contact;
import com.zherikhov.listshop.entity.ListShop;
import com.zherikhov.listshop.entity.Subscriber;
import com.zherikhov.listshop.service.button.InlineKeyButtonService;
import com.zherikhov.listshop.service.db.ContactService;
import com.zherikhov.listshop.service.db.ItemService;
import com.zherikhov.listshop.service.db.ListShopService;
import com.zherikhov.listshop.service.db.SubscriberService;
import com.zherikhov.listshop.utils.Check;
import com.zherikhov.listshop.utils.Resources;
import com.zherikhov.listshop.utils.TextFormat;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
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

    private final SendMessageController sendMessageController = new SendMessageController();
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
            if ((/*update.hasMessage() && update.getMessage().hasText() ||*/ update.hasCallbackQuery()) && subscriber.getMakeListStep() != 0) {
                makeList(update, subscriber);
            }

            if (update.hasMessage() && update.getMessage().hasText() && subscriber.getAddContactStep() != 0) {
                addContact(update, subscriber);
            }

            if (update.hasMessage() && update.getMessage().hasText() && subscriber.getFeedbackStep() != 0) {
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
                    execute(sendMessageController.createMessage(update, "Your help"));
                }
            }
            return;
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            switch (update.getMessage().getText()) {
                case "Make a list" -> {
                    log.info("Make a list from " + user.getUserName());

                    List<String> listShopNames = new ArrayList<>();
                    List<ListShop> listShops = listShopService.findAllByIdSubscriber(subscriber);
                    for (ListShop i : listShops) {
                        listShopNames.add(i.getName());
                    }

                    Objects.requireNonNull(subscriber).setMakeListStep(1);
                    subscriberService.save(subscriber);
                    execute(inlineKeyButtonService.setInlineButton(update, "Please select a list", listShopNames));
                }
                case "Add a contact" -> {
                    log.info("Add a contact from " + user.getUserName());

                    Objects.requireNonNull(subscriber).setAddContactStep(1);
                    subscriberService.save(subscriber);
                    execute(sendMessageController.createMessage(update, Messages.ADD_A_CONTACT));
                }
                case "Feedback" -> {
                    log.info("Feedback from " + user.getUserName());

                    Objects.requireNonNull(subscriber).setFeedbackStep(1);
                    subscriberService.save(subscriber);
                    execute(sendMessageController.createMessage(update, Messages.FEEDBACK));
                }
                case "About Bot" -> {
                    log.info("About Bot from " + user.getUserName());
                    execute(startCommand.start(update));
                }
            }
        }
    }

    public Subscriber findSubscriber(List<Subscriber> subscribers, User user) {
        for (Subscriber subscriber : subscribers) {
            if (subscriber.getId() == user.getId()) {
                return subscriber;
            }
        }
        return null;
    }

    @SneakyThrows
    public void makeList(Update update, Subscriber subscriber) {

        CallbackQuery callbackQuery = update.getCallbackQuery();
        String data = callbackQuery.getData();
        String chat_id = callbackQuery.getId();

        if (data.equals("list:Cancel")) {
            execute(sendMessageController.editInlineMessage(update, "..."));
//            itemService.findAllByIdListShop()
        }
    }

    @SneakyThrows
    public void addContact(Update update, Subscriber subscriber) {
        if (subscriber.getAddContactStep() == 1) {
            String userName = TextFormat.userNameFormat(update.getMessage().getText());
            Subscriber checkSubscriber = subscriberService.findByUserName(userName);

            if (checkSubscriber != null) {
                subscriber.setAddContactStep(2);
                subscriberService.save(subscriber);
                execute(sendMessageController.createMessage(update, Messages.WAIT_CONTACT));
            } else {
                subscriber.setAddContactStep(0);
                subscriberService.save(subscriber);
                execute(sendMessageController.createMessage(update, Messages.NOT_FOUND_NICK_NAME));
            }
        } else if (subscriber.getAddContactStep() == 2) {
            Contact contact = new Contact(subscriber, update.getMessage().getText(), subscriber.getUserName());
            contactService.save(contact);

            subscriber.setAddContactStep(0);
            subscriberService.save(subscriber);

            execute(sendMessageController.createMessage(update, Messages.WAIT_NICK_NAME));
        }
    }

    @SneakyThrows
    public void feedback(Update update, Subscriber subscriber) {
        execute(sendMessageController.createMessageForSupport(update.getMessage().getText()));

        subscriber.setFeedbackStep(0);
        subscriberService.save(subscriber);
        execute(sendMessageController.createMessage(update, Messages.FEEDBACK2));
    }

    @Override
    public String getBotUsername() {
        return Resources.getProperties("application.secure.properties", "telegram.name");
    }
}
