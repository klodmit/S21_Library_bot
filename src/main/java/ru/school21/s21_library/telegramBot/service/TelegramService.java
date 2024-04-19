package ru.school21.s21_library.telegramBot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.school21.s21_library.telegramBot.dto.AuthUserDto;
import ru.school21.s21_library.telegramBot.gateway.AuthRequest;

@Service
@RequiredArgsConstructor
public class TelegramService {
    private final AuthRequest authRequest;
    private String code;

    public void  sendCode(String nickName) {
        code = authRequest.request(AuthUserDto.builder().nickName(nickName).build());
    }

    public  boolean checkCode(String code) {
        return code.equals(this.code);
    }
}
