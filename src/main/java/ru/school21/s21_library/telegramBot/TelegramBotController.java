package ru.school21.s21_library.telegramBot;

import com.github.baloise.rocketchatrestclient.RocketChatClient;
import com.github.baloise.rocketchatrestclient.model.Room;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.school21.s21_library.telegramBot.service.TelegramService;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class TelegramBotController extends TelegramLongPollingBot {
    @Value("${bot.token}")
    private String botToken;
    @Value("${bot.name}")
    private String botName;

    private final TelegramService service;

    private static Set<String> set = new HashSet<>();
    private static Set<String> promt = new HashSet<>();
    private static Set<String> co = new HashSet<>();


    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update.getMessage().getText());
        Message message = update.getMessage();
        String chatId = String.valueOf(update.getMessage().getChatId());
        log.info("msg: {}, from {}", message.getText(), message.getFrom().getFirstName());


        switch (message.getText()) {
            case "/start":
                if (!set.contains(chatId) && !promt.contains(chatId)) {
                    //тут надо сходить в бд убедится что такого нет
                    sendMessage("Привет, ты у нас новенький! Можешь сказать свой логин", chatId);
                    promt.add(chatId);
                } else {
                    sendMessage("ты уже зареган, сори", chatId);
                }
                break;

            case "/showBooks" :
                if (set.contains(chatId)) {
                    sendMessage("Фича в разработке", chatId);
                }
                else {
                    sendMessage("Зарегайся", chatId);
                }
                break;
            case "/addBook" :
                if (set.contains(chatId)) {
                    sendMessage("Фича в разработке", chatId);
                }
                else {
                    sendMessage("Зарегайся", chatId);
                }
                break;
            case "/deleteBook" :
                if (set.contains(chatId)) {
                    sendMessage("Фича в разработке", chatId);
                }
                else {
                    sendMessage("Зарегайся", chatId);
                }
                break;
            case "/EditBook" :
                if (set.contains(chatId)) {
                    sendMessage("Фича в разработке", chatId);
                }
                else {
                    sendMessage("Зарегайся", chatId);
                }
                break;
            case "/ReserveBook" :
                if (set.contains(chatId)) {
                    sendMessage("Фича в разработке", chatId);
                }
                else {
                    sendMessage("Зарегайся", chatId);
                }
                break;
            case "/ReturnBook" :
                if (set.contains(chatId)) {
                    sendMessage("Фича в разработке", chatId);
                }
                else {
                    sendMessage("Зарегайся", chatId);
                }
                break;

            default:
                if (!set.contains(chatId)) {
                    if (!co.contains(chatId)) {
                        sendMessage("Введи пж код из рокета", chatId);
                        service.sendCode(message.getText());
                        co.add(chatId);

                    } else if (co.contains(chatId) && service.checkCode(message.getText())) {
                        sendMessage("Вы успешно вошли", chatId);
                        promt.remove(chatId);
                        set.add(chatId);
                        co.remove(chatId);
                    } else {
                        sendMessage("Код указан неверно", chatId);
                    }
                }
        }
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
