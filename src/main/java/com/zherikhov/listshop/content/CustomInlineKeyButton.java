package com.zherikhov.listshop.content;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class CustomInlineKeyButton {

    public List<List<InlineKeyboardButton>> createInlineButton(List<String> buttonsNames) {
        List<List<InlineKeyboardButton>> keyboardList = new ArrayList<>();

        for (String buttonName : buttonsNames) {
            List<InlineKeyboardButton> keyboardRow = new ArrayList<>();

            keyboardRow.add(new InlineKeyboardButton());
            keyboardRow.getFirst().setText(buttonName);
            keyboardRow.getFirst().setCallbackData("list:" + buttonName);
            keyboardList.add(keyboardRow);
        }

        List<InlineKeyboardButton> newButtonRow = new ArrayList<>();

        newButtonRow.add(new InlineKeyboardButton());
        newButtonRow.getFirst().setText("➕");
        newButtonRow.getFirst().setCallbackData("list:" + "Add");
        keyboardList.add(newButtonRow);

        List<InlineKeyboardButton> cancelButtonRow = new ArrayList<>();

        cancelButtonRow.add(new InlineKeyboardButton());
        cancelButtonRow.getFirst().setText("Close");
        cancelButtonRow.getFirst().setCallbackData("list:" + "Close");
        keyboardList.add(cancelButtonRow);

        return keyboardList;
    }

    public InlineKeyboardMarkup setKeyboardMarkup(List<List<InlineKeyboardButton>> keyboardList) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(keyboardList);
        return inlineKeyboardMarkup;
    }
}
