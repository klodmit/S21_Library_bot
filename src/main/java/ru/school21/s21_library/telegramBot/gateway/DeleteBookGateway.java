package ru.school21.s21_library.telegramBot.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.school21.s21_library.telegramBot.dto.BookDto;

@FeignClient(
        url = "http://localhost:8083",
        name = "delete"
)
public interface DeleteBookGateway {
    @DeleteMapping(value = "/deleteBook")
    String deleteBook(@RequestBody BookDto bookDto);
}
