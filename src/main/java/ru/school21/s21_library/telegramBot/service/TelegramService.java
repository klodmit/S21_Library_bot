package ru.school21.s21_library.telegramBot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.File;
import ru.school21.s21_library.telegramBot.dto.*;
import ru.school21.s21_library.telegramBot.gateway.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramService {
    private final AuthRequest authRequest;
    private final RegistrationRequest registrationRequest;

    private final CheckUserRequest checkUserRequest;
    private final ShowBooksGateway showBooksGateway;
    private final ReserveBookGateway reserveBookGateway;
    private final ReturnBookGateway returnBookGateway;
    private final AddBook addBook;
    private static Set<String> codeSet = new HashSet<>();

    public String  sendCode(String nickName) {
        String code = null;
        try {
             code = authRequest.request(AuthUserDto.builder().nickName(nickName).build());
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return "Bad Request";
        }
        codeSet.add(code);
        return "Success";
    }

    public boolean checkCode(String code) {
        return codeSet.contains(code);
    }

    public void UserRegistration(String nickName, String chatId) {
        RegistrationUserDto registrationUserDto = RegistrationUserDto.builder()
                .role("user")
                .nickname(nickName)
                .chatId(chatId)
                .build();
        registrationRequest.reg(registrationUserDto);
    }

    public MenuUserDto checkUser(CheckUserDto checkUserDto) {
        MenuUserDto res = checkUserRequest.checkUser(checkUserDto);
        log.info("ret {}", res);
        return res;
    }

    public String getBooks() {
        return showBooksGateway.showBooks();
    }

    public String makeReserve(BookDto bookDto) {
       return  reserveBookGateway.reserve(bookDto);
    }

    public String returnBook(BookDto bookDto) {
        return returnBookGateway.returnBook(bookDto);
    }
    public String addBook(BookDto bookDto) {
        return addBook.saveBook(bookDto);
    }




}
