package com.zherikhov.listshop.content;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class CustomKeyboardButton {

    public List<KeyboardRow> createKeyboardButtons(List<List<String>> buttonsNames) {
        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        for (List<String> rowButtons : buttonsNames) {
            keyboardRowList.add(new KeyboardRow());
            for (String buttonName : rowButtons) {
                keyboardRowList.get(keyboardRowList.size() - 1).add(new KeyboardButton(buttonName));
            }
        }
        return keyboardRowList;
    }

    public ReplyKeyboardMarkup setButtons(List<KeyboardRow> keyboardRowList) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }
}
