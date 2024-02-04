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
            Vau!

            This is Hello Message!""";

    public SendMessage start(Update update) {
        SendMessage sendMessage = messageController.createMessage(update, helloMessage);
        sendMessage.setParseMode(ParseMode.HTML);

        ReplyKeyboardMarkup keyboardMarkup =
                customKeyboardButton.setButtons(customKeyboardButton.createKeyboardButtons(new KeyboardsButtonNames().keyboardRows));

        sendMessage.setReplyMarkup(keyboardMarkup);
        return sendMessage;
    }
}
