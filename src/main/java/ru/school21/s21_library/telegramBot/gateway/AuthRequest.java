package ru.school21.s21_library.telegramBot.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.school21.s21_library.telegramBot.dto.AuthUserDto;

@FeignClient(
        url = "http://localhost:8080",
        name = "getAuth"
)
public interface AuthRequest {

    @PostMapping(value = "/registrationRequest")
     String request(@RequestBody AuthUserDto authUserDto);
}
