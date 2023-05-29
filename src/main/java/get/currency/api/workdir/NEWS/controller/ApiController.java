package get.currency.api.workdir.NEWS.controller;

import get.currency.api.workdir.NEWS.service.ArticleServiceForUpdateDB;
import get.currency.api.workdir.PINGER.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiController {
    private ArticleServiceForUpdateDB articleService;
    private final TelegramBot telegramBot;

    @Autowired
    public ApiController(ArticleServiceForUpdateDB articleService, TelegramBot telegramBot) {
        this.articleService = articleService;
        this.telegramBot = telegramBot;
    }

    @GetMapping("/apiKey")
    @PreAuthorize("hasAuthority('developers:write')")
    public String getApiKey() {
        return articleService.getApiKey();
    }

    @PostMapping("/apiKey")
    @PreAuthorize("hasAuthority('developers:write')")
    public void setApiKey(@RequestParam("apiKey") String apiKey) {
        telegramBot.sendTelegramMessage("Был обновлен ключ для получения новостей "+ apiKey);
        articleService.setApiKey(apiKey);
    }
}