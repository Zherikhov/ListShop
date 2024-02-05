package com.zherikhov.listshop.constants.buttons;

import java.util.ArrayList;
import java.util.List;

public class KeyboardsButtonNames {

    private final List<String> keyboardRowFirst = new ArrayList<>();

    {
        keyboardRowFirst.add("Make a list");
        keyboardRowFirst.add("Add a contact");
    }

    private final List<String> keyboardRowSecond = new ArrayList<>();

    {
        keyboardRowSecond.add("Feedback");
        keyboardRowSecond.add("About Bot");
    }

    public List<List<String>> keyboardRows = new ArrayList<>();

    {
        keyboardRows.add(keyboardRowFirst);
        keyboardRows.add(keyboardRowSecond);
    }
}
