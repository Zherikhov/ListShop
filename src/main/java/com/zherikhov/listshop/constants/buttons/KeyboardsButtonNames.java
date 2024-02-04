package com.zherikhov.listshop.constants.buttons;

import java.util.ArrayList;
import java.util.List;

public class KeyboardsButtonNames {

    private final List<String> keyboardRowFirst = new ArrayList<>();

    {
        keyboardRowFirst.add("Составить список");
        keyboardRowFirst.add("Добавить контакт");
    }

    private final List<String> keyboardRowSecond = new ArrayList<>();

    {
        keyboardRowSecond.add("Обратная связь");
        keyboardRowSecond.add("О боте");
    }

    public List<List<String>> keyboardRows = new ArrayList<>();

    {
        keyboardRows.add(keyboardRowFirst);
        keyboardRows.add(keyboardRowSecond);
    }
}
