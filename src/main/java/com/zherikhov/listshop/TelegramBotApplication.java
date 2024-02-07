package com.zherikhov.listshop;

import com.zherikhov.listshop.bufferClasses.SubscriberProperties;
import com.zherikhov.listshop.commands.Commands;
import com.zherikhov.listshop.commands.InlineKeyButtonService;
import com.zherikhov.listshop.commands.SendMessageController;
import com.zherikhov.listshop.constants.buttons.InlineButtonsNames;
import com.zherikhov.listshop.constants.text.Messages;
import com.zherikhov.listshop.entity.Contact;
import com.zherikhov.listshop.entity.Subscriber;
import com.zherikhov.listshop.service.ContactService;
import com.zherikhov.listshop.service.SubscriberService;
import com.zherikhov.listshop.utils.Check;
import com.zherikhov.listshop.utils.Resources;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TelegramBotApplication extends TelegramLongPollingBot {
    private final Commands startCommand = new Commands();

    private final SubscriberService subscriberService;
    private final ContactService contactService;
    private Subscriber subscriber = null;

    private final SendMessageController sendMessageController = new SendMessageController();
    private final List<SubscriberProperties> subscribers = new ArrayList<>();
    private final InlineKeyButtonService inlineKeyButtonService = new InlineKeyButtonService();

    public TelegramBotApplication(String botToken, SubscriberService service, ContactService contactService) {
        super(botToken);
        this.subscriberService = service;
        this.contactService = contactService;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        User user = update.getMessage().getFrom();
        SubscriberProperties currentSubscriberProperties;

        if (findSubscriber(subscribers, user) != null) {
            currentSubscriberProperties = findSubscriber(subscribers, user);
            log.info("Was found a subscriber from SUBSCRIBERS: " + currentSubscriberProperties.getId() + " " + user.getUserName());
        } else {
            currentSubscriberProperties = new SubscriberProperties(user.getId());
            currentSubscriberProperties.setId(user.getId());
            subscribers.add(currentSubscriberProperties);
            log.info("Added a subscriber to SUBSCRIBERS: " + currentSubscriberProperties.getId() + " " + user.getUserName());
        }

        if(update.hasMessage() && update.getMessage().hasText() && currentSubscriberProperties.isMakeList()) {
            makeList(update, user ,currentSubscriberProperties);
        }

        if (update.hasMessage() && update.getMessage().hasText() && currentSubscriberProperties.isAddContact()) {
            addContact(update, user ,currentSubscriberProperties);
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
                }
            }
            return;
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            switch (update.getMessage().getText()) {
                case "Make a list" -> {
                    log.info("Make a list from " + user.getUserName());
                    currentSubscriberProperties.setMakeList(true);
                    execute(inlineKeyButtonService.setInlineButton(update, "Please select a list", InlineButtonsNames.LIST_NAMES));
                }
                case "Add a contact" -> {
                    log.info("Add a contact from " + user.getUserName());
                    currentSubscriberProperties.setAddContact(true);
                    execute(sendMessageController.createMessage(update, Messages.ADD_A_CONTACT));

                }
                case "Feedback" -> {
                    log.info("Feedback from " + user.getUserName());
                    currentSubscriberProperties.setFeedback(true);
                    execute(sendMessageController.createMessage(update, Messages.FEEDBACK));
                }
                case "About Bot" -> {
                    log.info("About Bot from " + user.getUserName());
                    execute(startCommand.start(update));
                }
            }
        }
    }

    public SubscriberProperties findSubscriber(List<SubscriberProperties> subscribers, User user) {
        for (SubscriberProperties subscriber : subscribers) {
            if (subscriber.getId() == user.getId()) {
                return subscriber;
            }
        }
        return null;
    }

    public void makeList(Update update, User user, SubscriberProperties currentSubscriberProperties) {
        if (!currentSubscriberProperties.isWaitNickName() && subscriber == null) { //TODO:
            subscriber = subscriberService.findByUserName(update.getMessage().getText());
        }
    }

    @SneakyThrows
    public void addContact(Update update, User user, SubscriberProperties currentSubscriberProperties) {
        if (!currentSubscriberProperties.isWaitNickName() && subscriber == null) {
            subscriber = subscriberService.findByUserName(update.getMessage().getText());
        }

        if (subscriber != null && !currentSubscriberProperties.isWaitNickName()) {
            execute(sendMessageController.createMessage(update, Messages.WAIT_CONTACT));
            currentSubscriberProperties.setWaitNickName(true);
            return;
        }

        if (currentSubscriberProperties.isWaitNickName()) {
            Contact contact = new Contact(subscriberService.findById(user.getId()), update.getMessage().getText(), subscriber.getUserName());
            contactService.save(contact);
            currentSubscriberProperties.setWaitNickName(false);
            currentSubscriberProperties.setAddContact(false);
            subscriber = null;

            execute(sendMessageController.createMessage(update, Messages.WAIT_NICK_NAME));
            log.info("Contact was created for " + currentSubscriberProperties.getId());
            return;
        }

        execute(sendMessageController.createMessage(update, Messages.NOT_FOUND_NICK_NAME));
        currentSubscriberProperties.setAddContact(false);
    }

    @Override
    public String getBotUsername() {
        return Resources.getProperties("application.secure.properties", "telegram.name");
    }
}
