package get.currency.api.workdir.NEWS.controller;

import get.currency.api.workdir.NEWS.service.ArticleServiceForUpdateDB;
import get.currency.api.workdir.PINGER.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/news")
public class NewsUpdate {
    private ArticleServiceForUpdateDB articleServiceForUpdateDB;
    private final TelegramBot telegramBot;

    public NewsUpdate(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Autowired
    public void ArticleController(ArticleServiceForUpdateDB articleServiceForUpdateDB) {
        this.articleServiceForUpdateDB = articleServiceForUpdateDB;
    }




    @GetMapping("/getNews")
    @Scheduled(cron = "0 0 */5 ? * *")
    public void fetchAndSaveArticles() {
        telegramBot.sendTelegramMessage("Получение новостей, следующий запуск через 5 часов");
        articleServiceForUpdateDB.fetchAndSaveAllArticles();
    }

    @DeleteMapping("/delDuplicate")
    public void removeDuplicates() {
        articleServiceForUpdateDB.removeDuplicates();
    }




}