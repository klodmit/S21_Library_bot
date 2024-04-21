package ru.school21.s21_library.telegramBot.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.school21.s21_library.telegramBot.dto.BookDto;

@FeignClient(
        url = "http://localhost:8083",
        name = "return"
)
public interface ReturnBookGateway {
    @PostMapping(value = "/return-book")
    String returnBook(@RequestBody BookDto book);
}
