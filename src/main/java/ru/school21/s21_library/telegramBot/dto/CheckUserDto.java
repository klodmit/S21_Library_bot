package ru.school21.s21_library.telegramBot.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CheckUserDto {
    private String nickname;
    private String chatId;
}
