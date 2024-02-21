package com.zherikhov.listshop.service.button;

import com.zherikhov.listshop.service.sender.SendMessageService;
import com.zherikhov.listshop.content.CustomInlineKeyButton;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

public class InlineKeyButtonService {
    CustomInlineKeyButton inlineKeyButtons = new CustomInlineKeyButton();
    SendMessageService sendMessageController = new SendMessageService();
    InlineKeyboardMarkup keyboardMarkup;

    public SendMessage setInlineButton(Update update, String text, List<String> names) {

        SendMessage sendMessage = sendMessageController.createMessage(update, text);
        keyboardMarkup =
                inlineKeyButtons.setKeyboardMarkup(inlineKeyButtons.createInlineButton(names));

        sendMessage.setReplyMarkup(keyboardMarkup);
        return sendMessage;
    }

}