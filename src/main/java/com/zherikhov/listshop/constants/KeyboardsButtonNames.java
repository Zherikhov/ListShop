package com.zherikhov.listshop.constants;

import java.util.List;

public class KeyboardsButtonNames {

    private static final List<String> KEYBOARD_ROW_FIRST = List.of(
            "My lists",
            "Contacts"
    );

    private static final List<String> KEYBOARD_ROW_SECOND = List.of(
            "Feedback",
            "About Bot"
    );

    public static final List<List<String>> KEYBOARD_BUTTON_NAMES = List.of(
            KEYBOARD_ROW_FIRST,
            KEYBOARD_ROW_SECOND
    );

    public static final List<String> CONTACTS_INLINE_BUTTONS = List.of(
            "Add contact",
            "My contacts",
            "Rename contact",
            "Delete contact"
    );
}
