package ru.school21.s21_library.telegramBot.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrationUserDto {
    private String nickname;
    private String chatId;
    private String role;
}
