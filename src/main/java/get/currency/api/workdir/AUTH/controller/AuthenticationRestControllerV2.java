package get.currency.api.workdir.AUTH.controller;

import get.currency.api.workdir.AUTH.service.AuthenticationService;
import get.currency.api.workdir.PINGER.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v2/auth")
public class AuthenticationRestControllerV2 {
    private final AuthenticationService authenticationService;
    private final TelegramBot telegramBot;

    @Autowired
    public AuthenticationRestControllerV2(AuthenticationService authenticationService, TelegramBot telegramBot) {
        this.authenticationService = authenticationService;
        this.telegramBot = telegramBot;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(@RequestParam("email") String email,
                                          @RequestParam("password") String password,
                                          @RequestParam("first_name") String firstName,
                                          @RequestParam("last_name") String lastName) {
        telegramBot.sendTelegramMessage("Зарегистрирован новый пользователь: " + email);
        return authenticationService.registerUser(email, password, firstName, lastName);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDTO request) {
        return authenticationService.authenticate(request.getEmail(), request.getPassword());
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        authenticationService.logout(request, response);
    }
}