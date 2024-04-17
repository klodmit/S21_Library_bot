//package ru.school21.s21_library.telegramBot.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.meta.TelegramBotsApi;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//import org.telegram.telegrambots.meta.generics.LongPollingBot;
//import org.telegram.telegrambots.meta.generics.TelegramBot;
//import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
//
//@Component
//@RequiredArgsConstructor
//public class TelegramConfig {
//    private final  TelegramBot telegramBot;
//
//    @EventListener({ContextRefreshedEvent.class})
//    public void init() {
//
//        try {
//            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
//            botsApi.registerBot((LongPollingBot) telegramBot);
//        } catch (TelegramApiException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//}
