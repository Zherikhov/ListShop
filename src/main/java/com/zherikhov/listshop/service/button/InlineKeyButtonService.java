package com.zherikhov.listshop.service.button;

import com.zherikhov.listshop.service.sender.SendMessageService;
import com.zherikhov.listshop.content.CustomInlineKeyButton;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

public class InlineKeyButtonService {
    private final CustomInlineKeyButton customInlineKeyButton = new CustomInlineKeyButton();
    private final SendMessageService sendMessageController = new SendMessageService();
    private InlineKeyboardMarkup keyboardMarkup;

    public SendMessage setInlineButtonAllButtonsWithShare(Update update, String text, List<String> names) {

        SendMessage sendMessage = sendMessageController.createMessage(update, text);
        keyboardMarkup =
                customInlineKeyButton.setKeyboardMarkup(customInlineKeyButton.createInlineButtonWithShare(names));
        sendMessage.setReplyMarkup(keyboardMarkup);

        return sendMessage;
    }

    public SendMessage setInlineButtonAllButtonsWithoutShare(Update update, String text, List<String> names) {

        SendMessage sendMessage = sendMessageController.createMessage(update, text);
        keyboardMarkup =
                customInlineKeyButton.setKeyboardMarkup(customInlineKeyButton.createInlineButtonWithoutShare(names));
        sendMessage.setReplyMarkup(keyboardMarkup);

        return sendMessage;
    }

    public SendMessage setInlineButtonForDelete(Update update, String text, List<String> names) {

        SendMessage sendMessage = sendMessageController.createMessage(update, text);
        keyboardMarkup =
                customInlineKeyButton.setKeyboardMarkup(customInlineKeyButton.createInlineButtonForDelete(names));
        sendMessage.setReplyMarkup(keyboardMarkup);

        return sendMessage;
    }
}