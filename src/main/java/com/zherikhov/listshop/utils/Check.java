package com.zherikhov.listshop.utils;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.util.Optional;

public class Check {

    public static String checkCommand(Message message) {
        String command = null;
        if (message != null && message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> commandEntity =
                    message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (commandEntity.isPresent()) {
                command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
            }
        }
        return command;
    }
}