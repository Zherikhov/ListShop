package com.zherikhov.listshop.constants.button;

import java.util.List;

public class KeyboardsButtonNames {

    private static final List<String> KEYBOARD_ROW_FIRST = List.of(
            "Make a list",
            "Add a contact"
    );

    private static final List<String> KEYBOARD_ROW_SECOND = List.of(
            "Feedback",
            "About Bot"
    );

    public static final List<List<String>> keyboardRows = List.of(
            KEYBOARD_ROW_FIRST,
            KEYBOARD_ROW_SECOND
    );
}
