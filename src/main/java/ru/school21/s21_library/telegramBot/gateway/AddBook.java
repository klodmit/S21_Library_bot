package ru.school21.s21_library.telegramBot.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.school21.s21_library.telegramBot.dto.BookDto;

@FeignClient(
        url = "http://localhost:8083",
        name = "addBook"
)
public interface AddBook {
    @PostMapping(value = "/add-book")
    String saveBook(@RequestBody BookDto book);
}
