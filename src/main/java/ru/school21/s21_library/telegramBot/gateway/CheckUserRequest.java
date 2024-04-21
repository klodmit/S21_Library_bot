package ru.school21.s21_library.telegramBot.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.school21.s21_library.telegramBot.dto.AuthUserDto;
import ru.school21.s21_library.telegramBot.dto.CheckUserDto;
import ru.school21.s21_library.telegramBot.dto.MenuUserDto;

@FeignClient(
        url = "http://localhost:8083",
        name = "checkUSer"
)
public interface CheckUserRequest {
        @PostMapping(value = "/check-access")
        MenuUserDto checkUser(@RequestBody CheckUserDto checkUserDto);
}
