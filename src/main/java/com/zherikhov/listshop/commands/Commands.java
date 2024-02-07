package com.zherikhov.listshop.commands;

import com.zherikhov.listshop.constants.buttons.KeyboardsButtonNames;
import com.zherikhov.listshop.content.CustomKeyboardButton;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class Commands {
    CustomKeyboardButton customKeyboardButton = new CustomKeyboardButton();
    SendMessageController messageController = new SendMessageController();

    private final static String helloMessage = """
            Hello message!
            
            In the town where I was born
            Lived a man who sailed to sea
            And he told us of his life
            In the land of submarines
            So we sailed up to the sun
            Till we found a sea of green
            And we lived beneath the waves
            in our yellow submarine
                        
            We all live in a yellow submarine
            Yellow submarine, yellow submarine
            We all live in a yellow submarine
            Yellow submarine, yellow submarine""";

    public SendMessage start(Update update) {
        SendMessage sendMessage = messageController.createMessage(update, helloMessage);
        sendMessage.setParseMode(ParseMode.HTML);

        ReplyKeyboardMarkup keyboardMarkup =
                customKeyboardButton.setButtons(customKeyboardButton.createKeyboardButtons(KeyboardsButtonNames.keyboardRows));

        sendMessage.setReplyMarkup(keyboardMarkup);
        return sendMessage;
    }
}
