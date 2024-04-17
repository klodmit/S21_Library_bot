package ru.school21.s21_library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.school21.s21_library.telegramBot.TelegramBotController;

@SpringBootApplication
public class S21LibraryApplication {

    public static void main(String[] args) throws TelegramApiException {
        SpringApplication.run(S21LibraryApplication.class, args);
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new TelegramBotController());
    }

}
