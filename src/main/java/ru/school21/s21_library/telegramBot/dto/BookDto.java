package ru.school21.s21_library.telegramBot.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookDto {
    private String author;
    private String title;
    private String chatId;
    private String genre;
    private int rating;
    private int copies;

    private Long id;
}
