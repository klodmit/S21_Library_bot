package ru.school21.s21_library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.school21.s21_library.telegramBot.TelegramBotController;
import ru.school21.s21_library.telegramBot.service.TelegramService;

@SpringBootApplication
@EnableFeignClients
public class S21LibraryApplication {

    public static void main(String[] args) throws TelegramApiException {
        var ctx = SpringApplication.run(S21LibraryApplication.class, args);
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(ctx.getBean("telegramBotController", TelegramLongPollingBot.class));
    }

}
