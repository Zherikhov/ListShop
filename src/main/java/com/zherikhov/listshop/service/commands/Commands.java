package com.zherikhov.listshop.service.commands;

import com.zherikhov.listshop.constants.button.KeyboardsButtonNames;
import com.zherikhov.listshop.content.CustomKeyboardButton;
import com.zherikhov.listshop.service.sender.SendMessageService;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class Commands {
    CustomKeyboardButton customKeyboardButton = new CustomKeyboardButton();
    SendMessageService messageController = new SendMessageService();

    private final static String helloMessage = """
            Hello!
                                              
            This app will help you create a shopping list and share it with your contacts, but the app is still under development and will be ready 01/03/24.
                                              
            Best wishes, Vlad.""";

    public SendMessage start(Update update) {
        SendMessage sendMessage = messageController.createMessage(update, helloMessage);
        sendMessage.setParseMode(ParseMode.HTML);

        ReplyKeyboardMarkup keyboardMarkup =
                customKeyboardButton.setButtons(customKeyboardButton.createKeyboardButtons(KeyboardsButtonNames.keyboardRows));

        sendMessage.setReplyMarkup(keyboardMarkup);
        return sendMessage;
    }
}
