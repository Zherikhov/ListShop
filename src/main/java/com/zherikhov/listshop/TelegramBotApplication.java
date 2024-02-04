package com.zherikhov.listshop;

import com.zherikhov.listshop.commands.Commands;
import com.zherikhov.listshop.dao.SubscriberRepository;
import com.zherikhov.listshop.entity.Subscriber;
import com.zherikhov.listshop.service.SubscriberService;
import com.zherikhov.listshop.utils.Check;
import com.zherikhov.listshop.utils.Resources;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramBotApplication extends TelegramLongPollingBot {
    private final Commands startCommand = new Commands();

//    private SubscriberService service;

    public TelegramBotApplication(String botToken) {
        super(botToken);
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        String command = Check.checkCommand(update.getMessage());
        if (!command.isEmpty()) {
            switch (command) {
                case "/start" -> {
                    execute(startCommand.start(update));
//                    service.save(new Subscriber(10005, "q", "w", "e"));

                }
                case "/help" -> {

                }
            }
            return;
        }
        System.out.println("!");
    }

    @Override
    public String getBotUsername() {
        return Resources.getProperties("application.secure.properties", "telegram.name");
    }
}
