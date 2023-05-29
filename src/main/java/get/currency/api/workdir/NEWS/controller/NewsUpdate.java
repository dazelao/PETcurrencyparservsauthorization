package get.currency.api.workdir.NEWS.controller;

import get.currency.api.workdir.NEWS.service.ArticleServiceForUpdateDB;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/news")
public class NewsUpdate {
    private ArticleServiceForUpdateDB articleServiceForUpdateDB;

    @Autowired
    public void ArticleController(ArticleServiceForUpdateDB articleServiceForUpdateDB) {
        this.articleServiceForUpdateDB = articleServiceForUpdateDB;
    }




    @GetMapping("/getNews")
//    @Scheduled(cron = "0 */15 * ? * *")
    public void fetchAndSaveArticles() {
        articleServiceForUpdateDB.fetchAndSaveAllArticles();
    }

    @DeleteMapping("/delDuplicate")
//    @Scheduled(fixedDelay = 30000)
    public void removeDuplicates() {
        articleServiceForUpdateDB.removeDuplicates();
    }


//    @Scheduled(cron = "0 */10 * ? * *")
//    @GetMapping("/getNews")
//    public void fetchAndSaveArticles() {
//        CompletableFuture.runAsync(() -> {
//            articleService.fetchAndSaveAllArticles();
//            articleService.removeDuplicates();
//        });
//    }

}