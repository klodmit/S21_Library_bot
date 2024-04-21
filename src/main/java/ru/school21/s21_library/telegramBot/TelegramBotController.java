package ru.school21.s21_library.telegramBot;

import com.github.baloise.rocketchatrestclient.RocketChatClient;
import com.github.baloise.rocketchatrestclient.model.Room;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import ru.school21.s21_library.telegramBot.dto.BookDto;
import ru.school21.s21_library.telegramBot.dto.CheckUserDto;
import ru.school21.s21_library.telegramBot.dto.MenuUserDto;
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

    private static Map<String, MenuUserDto> startAuth = new HashMap<>();
    private static Set<String> promtToConnect = new HashSet<>();
    private static Map<String, String> waitCode = new HashMap<>();
    private static Map<String, String> prevCommand = new HashMap<>();


    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage() != null) {
            log.info(update.getMessage().getText() + " " + update.getMessage().getFrom().getFirstName());
            String chatId = String.valueOf(update.getMessage().getChatId());
            Message message = update.getMessage();
            textHandler(message, chatId);
        } else {
            log.info(update.getCallbackQuery().getData() + " " + update.getCallbackQuery().getFrom().getFirstName());
            String message = update.getCallbackQuery().getData();
           String chatId =  String.valueOf(update.getCallbackQuery().getMessage().getChatId());
            try {
                commandHandler(message, chatId);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
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
        sendMessage.enableHtml(true);
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
    public void sendMenu(MenuUserDto menuUserDto) {
        var showBook = InlineKeyboardButton.builder()
                .text("Книги").callbackData("/showBooks")
                .build();
        var reserveBook = InlineKeyboardButton.builder()
                .text("Резерв").callbackData("/ReserveBook")
                .build();
        var returnBook = InlineKeyboardButton.builder()
                .text("Вернуть").callbackData("/ReturnBook")
                .build();


        SendMessage sendMessage = new SendMessage();
        InlineKeyboardMarkup keyboardM1;

        if (menuUserDto.getRole().equals("user")) {
            keyboardM1 = InlineKeyboardMarkup.builder()
                    .keyboardRow(List.of(showBook, reserveBook, returnBook)).build();
            sendMessage.setText("User menu");
            sendMessage.setParseMode("HTML");
            sendMessage.setChatId(menuUserDto.getChatId());
            sendMessage.setReplyMarkup(keyboardM1);
        }
        else {
            var editBook = InlineKeyboardButton.builder()
                    .text("Ред Книгу").callbackData("/EditBook")
                    .build();
            var addBook = InlineKeyboardButton.builder()
                    .text("доб Книгу").callbackData("/addBook")
                    .build();
            var deleteBook = InlineKeyboardButton.builder()
                    .text("удалить").callbackData("/deleteBook")
                    .build();
            keyboardM1 = InlineKeyboardMarkup.builder()
                    .keyboardRow(List.of(showBook, reserveBook))
                    .keyboardRow(List.of(returnBook, editBook))
                    .keyboardRow(List.of(addBook))
                    .keyboardRow(List.of(deleteBook)).build();
            sendMessage.setText("Admin menu");
            sendMessage.setParseMode("HTML");
            sendMessage.setChatId(menuUserDto.getChatId());
            sendMessage.setReplyMarkup(keyboardM1);
        }
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
                if (!startAuth.containsKey(chatId) && !promtToConnect.contains(chatId)) {
                    MenuUserDto menuUserDto = service.checkUser(CheckUserDto.builder()
                            .chatId(chatId)
                            .build());
                    if (menuUserDto != null) {
                        startAuth.put(chatId, menuUserDto);
                        sendMenu( menuUserDto);
                    } else {
                        sendMessage("Привет, ты у нас новенький! Можешь сказать свой логин", chatId);
                        promtToConnect.add(chatId);
                    }
                } else if (!startAuth.containsKey(chatId)) {
                    sendMessage("пройди регу, *_* -> напиши свой ник", chatId);
                } else {
                    sendMenu(startAuth.get(chatId));
                }
                break;

            default:
                if (!startAuth.containsKey(chatId)) {
                    if (!waitCode.containsKey(chatId)) {
                        if (service.sendCode(message.getText()).equals("Success")) {
                            waitCode.put(chatId, message.getText());
                            sendMessage("Введи пж код из рокета", chatId);
                        } else if (promtToConnect.contains(chatId)) {
                            sendMessage("Укажи норм ник", chatId);
                        }
                        else {
                            sendMenu(startAuth.get(chatId));
                        }

                    } else if (waitCode.containsKey(chatId) && service.checkCode(message.getText())) {
                        service.UserRegistration(waitCode.get(chatId), chatId);
                        MenuUserDto menuUserDto = service.checkUser(CheckUserDto.builder()
                                .chatId(chatId)
                                .build());
                        sendMenu(menuUserDto);
                        promtToConnect.remove(chatId);
                        startAuth.put(chatId, menuUserDto);
                        waitCode.remove(chatId);
                    } else {
                        sendMessage("Код указан неверно", chatId);
                    }
                }
                else {
                    if (prevCommand.get(chatId).equals("/ReserveBook")) {
                        String[] split = null;
                        BookDto bookDto;
                        if(!StringUtils.isNumeric(message.getText())) {
                             split = message.getText().split("/");
                            bookDto = BookDto.builder()
                                    .chatId(chatId)
                                    .title(split[0])
                                    .author(split[1])
                                    .build();
                        }
                       else {
                            bookDto = BookDto.builder()
                                    .chatId(chatId)
                                    .id(Long.parseLong(message.getText()))
                                    .build();
                        }
                        service.makeReserve(bookDto);
                       sendMenu(startAuth.get(chatId));
                    }
                    else if (prevCommand.get(chatId).equals("/ReturnBook")) {
                        String[] split = null;
                        BookDto bookDto;
                        if(!StringUtils.isNumeric(message.getText())) {
                            split = message.getText().split("/");
                            bookDto = BookDto.builder()
                                    .chatId(chatId)
                                    .title(split[0])
                                    .author(split[1])
                                    .build();
                        }
                        else {
                            bookDto = BookDto.builder()
                                    .chatId(chatId)
                                    .id(Long.parseLong(message.getText()))
                                    .build();
                        }
                        service.returnBook(bookDto);
                        sendMenu(startAuth.get(chatId));
                    }
                    else if (prevCommand.get(chatId).equals("/addBook")) {
                        //title/author/rating/copies/genre
                        String[] split =  message.getText().split("/");
                        BookDto bookDto = BookDto.builder()
                                .title(split[0])
                                .author(split[1])
                                .rating(Integer.parseInt(split[2]))
                                .copies(Integer.parseInt(split[3]))
                                .genre(split[4])
                                .build();
                        service.addBook(bookDto);
                        sendMenu(startAuth.get(chatId));
                    }
                    else if (prevCommand.get(chatId).equals("/deleteBook")) {
                        BookDto bookDto = BookDto.builder()
                                .id(Long.parseLong(message.getText()))
                                .build();
                        service.deleteBook(bookDto);
                        sendMenu(startAuth.get(chatId));
                    }
                    else if(prevCommand.get(chatId).equals("/EditBook")) {
                        String[] split = message.getText().split("/");
                        BookDto bookDto = BookDto.builder()
                                .id(Long.parseLong(split[0]))
                                .title(split[1])
                                .author(split[2])
                                .rating(Integer.parseInt(split[3]))
                                .copies(Integer.parseInt(split[4]))
                                .genre(split[5])
                                .build();
                        service.editBook(bookDto);
                        sendMenu(startAuth.get(chatId));
                    }
                }
        }
    }

    private void commandHandler(String command, String chatId) throws TelegramApiException {
        switch (command) {
            case "/showBooks":
                if (startAuth.containsKey(chatId)) {
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
                }
                else {
                    sendMessage("Напиши /start для выхода в menu(ваша сессия истекла)", chatId);
                }
//
                break;
            case "/addBook":
                if (startAuth.containsKey(chatId)) {
                    if (startAuth.containsKey(chatId) ) {
                        prevCommand.put(chatId, "/addBook");
                        sendMessage("введите данные о книге в таком формате: title/author/rating/copies/genre", chatId);
                    }
                } else {
                    sendMessage("Напиши /start для выхода в menu(ваша сессия истекла)", chatId);
                }
                break;
            case "/deleteBook":
                if (startAuth.containsKey(chatId)) {
                    prevCommand.put(chatId, "/deleteBook");
                    sendMessage("введите номер книги из общего списка(кнопка книги)", chatId);
                } else {
                    sendMessage("Напиши /start для выхода в menu(ваша сессия истекла)", chatId);
                }
                break;
            case "/EditBook":
                if (startAuth.containsKey(chatId)) {
                    prevCommand.put(chatId, "/EditBook");
                    sendMessage("введите номер книги и что будете менять формат number/title/author/rating/copies/genre", chatId);
                } else {
                    sendMessage("Напиши /start для выхода в menu(ваша сессия истекла)", chatId);
                }
                break;
            case "/ReserveBook":
                if (startAuth.containsKey(chatId) ) {
                    prevCommand.put(chatId, "/ReserveBook");
                    sendMessage("введите номер книги или название кинги + автор в формате title/author", chatId);
                } else {
                    sendMessage("Напиши /start для выхода в menu(ваша сессия истекла)", chatId);
                }
                break;
            case "/ReturnBook":
                if (startAuth.containsKey(chatId)) {
                    prevCommand.put(chatId, "/ReturnBook");
                    sendMessage("введите номер книги в общем списке или название кинги + автор в формате title/author", chatId);
                } else {
                    sendMessage("Напиши /start для выхода в menu(ваша сессия истекла)", chatId);
                }
                break;
            default:

        }
    }
}


