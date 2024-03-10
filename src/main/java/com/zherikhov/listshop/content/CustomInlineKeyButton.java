package com.zherikhov.listshop.content;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.zherikhov.listshop.constants.TextMessage.*;

public class CustomInlineKeyButton {

    public List<List<InlineKeyboardButton>> createInlineButton(List<String> buttonsNames) {
        List<List<InlineKeyboardButton>> keyboardList = new ArrayList<>();

        for (String buttonName : buttonsNames) {
            List<InlineKeyboardButton> keyboardRow = new ArrayList<>();

            keyboardRow.add(new InlineKeyboardButton());
            keyboardRow.getFirst().setText(buttonName);
            keyboardRow.getFirst().setCallbackData("name:" + buttonName);

            keyboardList.add(keyboardRow);
        }

        List<InlineKeyboardButton> newButtonRow = new ArrayList<>();

        newButtonRow.add(new InlineKeyboardButton());
        newButtonRow.getFirst().setText("➕");
        newButtonRow.getFirst().setCallbackData("function:" + ADD);
        keyboardList.add(newButtonRow);

        List<InlineKeyboardButton> deleteButtonRow = new ArrayList<>();

        deleteButtonRow.add(new InlineKeyboardButton());
        deleteButtonRow.getFirst().setText("❌");
        deleteButtonRow.getFirst().setCallbackData("function:" + DELETE);
        keyboardList.add(deleteButtonRow);

        List<InlineKeyboardButton> cancelButtonRow = new ArrayList<>();

        cancelButtonRow.add(new InlineKeyboardButton());
        cancelButtonRow.getFirst().setText(CLOSE);
        cancelButtonRow.getFirst().setCallbackData("function:" + CLOSE);
        keyboardList.add(cancelButtonRow);

        return keyboardList;
    }

    public List<List<InlineKeyboardButton>> createInlineButtonForDelete(List<String> buttonsNames) {
        List<List<InlineKeyboardButton>> keyboardList = new ArrayList<>();

        for (String buttonName : buttonsNames) {
            List<InlineKeyboardButton> keyboardRow = new ArrayList<>();

            keyboardRow.add(new InlineKeyboardButton());
            keyboardRow.getFirst().setText(buttonName);
            keyboardRow.getFirst().setCallbackData("name:" + buttonName);

            keyboardList.add(keyboardRow);
        }

        List<InlineKeyboardButton> deleteButtonRow = new ArrayList<>();

        deleteButtonRow.add(new InlineKeyboardButton());
        deleteButtonRow.getFirst().setText(CLOSE);
        deleteButtonRow.getFirst().setCallbackData("function:" + CLOSE);
        keyboardList.add(deleteButtonRow);

        return keyboardList;
    }

    public InlineKeyboardMarkup setKeyboardMarkup(List<List<InlineKeyboardButton>> keyboardList) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(keyboardList);
        return inlineKeyboardMarkup;
    }
}
