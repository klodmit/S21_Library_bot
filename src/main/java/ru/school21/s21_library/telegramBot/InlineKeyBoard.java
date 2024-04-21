package ru.school21.s21_library.telegramBot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
@Component
public class InlineKeyBoard {

    public  List<KeyboardRow> createInlineKeyboard(List<String> buttonNames) {
        List<KeyboardRow> keyboard = new ArrayList<>();

        for (String buttonName : buttonNames) {
            InlineKeyboardButton inlineButton = new InlineKeyboardButton();
            inlineButton.setText(buttonName);
            inlineButton.setCallbackData(buttonName);

            KeyboardButton keyboardButton = new KeyboardButton();
            keyboardButton.setText(inlineButton.getText());
            keyboardButton.setRequestContact(true); // Устанавливаем true для запроса контакта

            KeyboardRow row = new KeyboardRow();
            row.add(keyboardButton);

            keyboard.add(row);
        }

        return keyboard;
    }
}