package ru.school21.s21_library.telegramBot;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

public class TelegramBotController extends TelegramLongPollingBot {
    @Value("${bot.token}")
    private String botToken;
    @Value("${bot.name}")
    private String botName;

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update.getMessage().getText());
        sendMessage("hello", String.valueOf(update.getMessage().getChatId()));
    }

    @Override
    public String getBotUsername() {
        return "s21_library_bot";
    }

    @Override
    public String getBotToken() {
        return "7176076647:AAE-CJh67Hn9KKUDAkejS2YFHlASAoWKwxI";
    }

    @SneakyThrows
    public void sendMessage(String text, String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(chatId);
        execute(sendMessage);
    }
}
