package com.zherikhov.listshop.commands;

import com.zherikhov.listshop.content.CustomInlineKeyButton;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

public class InlineKeyButtonService {
    CustomInlineKeyButton inlineKeyButtons = new CustomInlineKeyButton();
    SendMessageController sendMessageController = new SendMessageController();
    InlineKeyboardMarkup keyboardMarkup;

    public SendMessage setInlineButton(Update update, String text, List<String> names) {

        SendMessage sendMessage = sendMessageController.createMessage(update, text);
        keyboardMarkup =
                inlineKeyButtons.setKeyboardMarkup(inlineKeyButtons.test(names));

        sendMessage.setReplyMarkup(keyboardMarkup);
        return sendMessage;
    }

}