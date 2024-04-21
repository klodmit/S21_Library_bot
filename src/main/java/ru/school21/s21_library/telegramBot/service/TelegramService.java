package ru.school21.s21_library.telegramBot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.File;
import ru.school21.s21_library.telegramBot.dto.AuthUserDto;
import ru.school21.s21_library.telegramBot.dto.Book;
import ru.school21.s21_library.telegramBot.dto.CheckUserDto;
import ru.school21.s21_library.telegramBot.dto.RegistrationUserDto;
import ru.school21.s21_library.telegramBot.gateway.AuthRequest;
import ru.school21.s21_library.telegramBot.gateway.CheckUserRequest;
import ru.school21.s21_library.telegramBot.gateway.RegistrationRequest;
import ru.school21.s21_library.telegramBot.gateway.ShowBooksGateway;

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

    public boolean checkUser(CheckUserDto checkUserDto) {
        String res = checkUserRequest.checkUser(checkUserDto);
        log.info("ret {}", res);
        return checkUserRequest.checkUser(checkUserDto).equals("Success");
    }

    public String getBooks() {
        return showBooksGateway.showBooks();
    }


}
