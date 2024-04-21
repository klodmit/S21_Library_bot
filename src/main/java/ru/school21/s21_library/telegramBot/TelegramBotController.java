package ru.school21.s21_library.telegramBot;

import com.github.baloise.rocketchatrestclient.RocketChatClient;
import com.github.baloise.rocketchatrestclient.model.Room;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.school21.s21_library.telegramBot.dto.Book;
import ru.school21.s21_library.telegramBot.dto.CheckUserDto;
import ru.school21.s21_library.telegramBot.service.TelegramService;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
    private final InlineKeyBoard inlineKeyBoard;

    private static Set<String> startAuth = new HashSet<>();
    private static Set<String> promtToConnect = new HashSet<>();
    private static Map<String, String> waitCode = new HashMap<>();


    @Override
    public void onUpdateReceived(Update update) {


        if (update.getMessage() != null) {
            String chatId = String.valueOf(update.getMessage().getChatId());
            Message message = update.getMessage();
            textHandler(message, chatId);
        } else {
            String message = update.getCallbackQuery().getData();
           String chatId =  String.valueOf(update.getCallbackQuery().getMessage().getChatId());
            commandHandler(message, chatId);
        }

//        log.info("msg: {}, from {}", message.getText(), message.getFrom().getFirstName());


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

    public void sendFile(File file, String chatId) throws TelegramApiException {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);
        InputFile inputFile = new InputFile(file, "Книги.html");
        sendDocument.setDocument(inputFile);
        execute(sendDocument);
    }

    @SneakyThrows
    public void sendMessage(String text, String chatId, boolean r) {
        var next = InlineKeyboardButton.builder()
                .text("Показать Книги").callbackData("/showBooks")
                .build();

        InlineKeyboardMarkup keyboardM1;
        keyboardM1 = InlineKeyboardMarkup.builder()

                .keyboardRow(List.of(next)).build();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setParseMode("HTML");
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(keyboardM1);
        execute(sendMessage);
    }

    @SneakyThrows
    public void sendCollection(List<?> coll, String chatId) {
        coll.stream().map(x -> {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(x.toString());
            sendMessage.setParseMode("HTML");
            return sendMessage;
        }).forEach(x -> {
            try {
                execute(x);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void textHandler(Message message, String chatId) {
        switch (message.getText()) {
            case "/start":
                if (!startAuth.contains(chatId) && !promtToConnect.contains(chatId)) {
                    if (service.checkUser(CheckUserDto.builder()
                            .chatId(chatId)
                            .build())) {
                        startAuth.add(chatId);
                        sendMessage("<b>Menu 1</b>", chatId, true);
                    } else {
                        sendMessage("Привет, ты у нас новенький! Можешь сказать свой логин", chatId);
                        promtToConnect.add(chatId);
                    }
                } else if (!startAuth.contains(chatId)) {
                    sendMessage("пройди регу, *_* -> напиши свой ник", chatId);
                } else {
                    sendMessage("<b>Menu 1</b>", chatId, true);
                    startAuth.add(chatId);
                }
                break;

            case "/showBooks":
                if (startAuth.contains(chatId)) {
                    String test = service.getBooks();
                    try {
                        File testFile = File.createTempFile("test", ".html");
                        try (OutputStreamWriter outputStream = new OutputStreamWriter(new FileOutputStream(testFile), StandardCharsets.UTF_8);
                             BufferedWriter writer = new BufferedWriter(outputStream)) {
                            writer.write(test);
                            writer.flush();
                        }
                        sendFile(testFile, chatId);
                    } catch (IOException | TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
//                    BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream());
//                    sendCollection(service.getBooks(), chatId);
                } else {
                    sendMessage("Напиши /start для выхода в menu", chatId);
                }
                break;
            case "/addBook":
                if (startAuth.contains(chatId)) {
                    sendMessage("Фича в разработке", chatId);
                } else {
                    sendMessage("Зарегайся", chatId);
                }
                break;
            case "/deleteBook":
                if (startAuth.contains(chatId)) {
                    sendMessage("Фича в разработке", chatId);
                } else {
                    sendMessage("Зарегайся", chatId);
                }
                break;
            case "/EditBook":
                if (startAuth.contains(chatId)) {
                    sendMessage("Фича в разработке", chatId);
                } else {
                    sendMessage("Зарегайся", chatId);
                }
                break;
            case "/ReserveBook":
                if (startAuth.contains(chatId)) {
                    sendMessage("Фича в разработке", chatId);
                } else {
                    sendMessage("Зарегайся", chatId);
                }
                break;
            case "/ReturnBook":
                if (promtToConnect.contains(chatId)) {
                    sendMessage("Фича в разработке", chatId);
                } else {
                    sendMessage("Зарегайся", chatId);
                }
                break;

            default:
                if (!startAuth.contains(chatId)) {
                    if (!waitCode.containsKey(chatId)) {
                        if (service.sendCode(message.getText()).equals("Success")) {
                            waitCode.put(chatId, message.getText());
                            sendMessage("Введи пж код из рокета", chatId);
                        } else if (promtToConnect.contains(chatId)) {
                            sendMessage("Укажи норм ник", chatId);
                        }

                    } else if (waitCode.containsKey(chatId) && service.checkCode(message.getText())) {
                        service.UserRegistration(waitCode.get(chatId), chatId);
                        sendMessage("<b>Menu 1</b>", chatId, true);
                        promtToConnect.remove(chatId);
                        startAuth.add(chatId);
                        waitCode.remove(chatId);
                    } else {
                        sendMessage("Код указан неверно", chatId);
                    }
                }
        }
    }

    private void commandHandler(String command, String chatId) {
        switch (command) {
            case "/showBooks":
                String test = service.getBooks();
                try {
                    File testFile = File.createTempFile("test", ".html");
                    try (OutputStreamWriter outputStream = new OutputStreamWriter(new FileOutputStream(testFile), StandardCharsets.UTF_8);
                         BufferedWriter writer = new BufferedWriter(outputStream)) {
                        writer.write(test);
                        writer.flush();
                    }
                    sendFile(testFile, chatId);
                } catch (IOException | TelegramApiException e) {
                    throw new RuntimeException(e);
                }
//                    BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream());
//                    sendCollection(service.getBooks(), chatId);
                break;
            case "/addBook":
                if (startAuth.contains(chatId)) {
                    sendMessage("Фича в разработке", chatId);
                } else {
                    sendMessage("Зарегайся", chatId);
                }
                break;
            case "/deleteBook":
                if (startAuth.contains(chatId)) {
                    sendMessage("Фича в разработке", chatId);
                } else {
                    sendMessage("Зарегайся", chatId);
                }
                break;
            case "/EditBook":
                if (startAuth.contains(chatId)) {
                    sendMessage("Фича в разработке", chatId);
                } else {
                    sendMessage("Зарегайся", chatId);
                }
                break;
            case "/ReserveBook":
                if (startAuth.contains(chatId)) {
                    sendMessage("Фича в разработке", chatId);
                } else {
                    sendMessage("Зарегайся", chatId);
                }
                break;
            case "/ReturnBook":
                if (promtToConnect.contains(chatId)) {
                    sendMessage("Фича в разработке", chatId);
                } else {
                    sendMessage("Зарегайся", chatId);
                }
                break;

            default:

        }
    }
}


