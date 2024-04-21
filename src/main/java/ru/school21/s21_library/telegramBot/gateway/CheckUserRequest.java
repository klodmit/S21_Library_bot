package ru.school21.s21_library.telegramBot.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.school21.s21_library.telegramBot.dto.CheckUserDto;

@FeignClient(
        url = "http://localhost:8083",
        name = "checkUSer"
)
public interface CheckUserRequest {
        @PostMapping(value = "/check-access")
     String checkUser(@RequestBody CheckUserDto checkUserDto);
}
