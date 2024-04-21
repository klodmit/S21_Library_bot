package ru.school21.s21_library.telegramBot.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.school21.s21_library.telegramBot.dto.RegistrationUserDto;

@FeignClient(
        url = "http://localhost:8083",
        name = "regUser"
)
public interface RegistrationRequest {
    @PostMapping(value = "/reg-User")
     String reg(@RequestBody RegistrationUserDto registrationUserDto);
}
