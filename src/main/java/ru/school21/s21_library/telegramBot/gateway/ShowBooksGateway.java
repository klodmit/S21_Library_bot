package ru.school21.s21_library.telegramBot.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import ru.school21.s21_library.telegramBot.dto.Book;

import java.util.List;

@FeignClient(
        url = "http://91.186.197.234:8080",
        name = "showBooks"
)
public interface ShowBooksGateway {
    @GetMapping(value = "/books")
    String showBooks();
}
