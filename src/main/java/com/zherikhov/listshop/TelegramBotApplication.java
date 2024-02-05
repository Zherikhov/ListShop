package com.zherikhov.listshop;

import com.zherikhov.listshop.commands.Commands;
import com.zherikhov.listshop.entity.Subscriber;
import com.zherikhov.listshop.service.SubscriberService;
import com.zherikhov.listshop.utils.Check;
import com.zherikhov.listshop.utils.Resources;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Slf4j
public class TelegramBotApplication extends TelegramLongPollingBot {
    private final Commands startCommand = new Commands();

    private final SubscriberService service;

    public TelegramBotApplication(String botToken, SubscriberService service) {
        super(botToken);
        this.service = service;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        User user = update.getMessage().getFrom();

        String command = Check.checkCommand(update.getMessage());
        if (command != null) {
            switch (command) {
                case "/start" -> {
                    log.info("/start -> " + user.getId());

                    if (service.findById(user.getId()) == null) {
                        service.save(new Subscriber(user.getId(), user.getUserName(), user.getFirstName(), user.getLastName()));
                    }
                    execute(startCommand.start(update));
                }
                case "/help" -> {
                    log.info("/help -> " + user.getId());
                }
            }
            return;
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            switch (update.getMessage().getText()) {
                case "Make a list" -> {
                    log.info("Make a list");
                }
                case "Add a contact" -> {
                    log.info("Add a contact");
                }
                case "Feedback" -> {
                    log.info("Feedback");
                }
                case "About Bot" -> {
                    log.info("About Bot");
                }
            }
        }
    }


    @Override
    public String getBotUsername() {
        return Resources.getProperties("application.secure.properties", "telegram.name");
    }
}
