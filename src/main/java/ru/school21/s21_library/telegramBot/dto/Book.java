package ru.school21.s21_library.telegramBot.dto;

import lombok.Data;

@Data
public class Book {
    private String author;
    private String title;
    private String genre;
    private int copies;
    private int rating;
}
