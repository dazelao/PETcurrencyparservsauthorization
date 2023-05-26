package get.currency.api.workdir.NEWS.controller;

import get.currency.api.workdir.NEWS.model.Article;
import get.currency.api.workdir.NEWS.service.ArticleServiceForUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/news")
public class ArticleController {

    private final ArticleServiceForUser articleServiceForUser;

    public ArticleController(ArticleServiceForUser articleServiceForUser) {
        this.articleServiceForUser = articleServiceForUser;
    }

    @GetMapping("/id")
    public ResponseEntity<Optional<Article>> getById(@RequestParam("id") Long id){
        Optional<Article> news = articleServiceForUser.getByid(id);
        return ResponseEntity.ok(news);
    }

    @GetMapping("newsbyparam")
    public ResponseEntity<List<Article>> getArticlesByParameter(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "country", required = false) String country) {

        List<Article> articles;

        if (category != null) {
            articles = articleServiceForUser.getArticlesByCategory(category);
        } else if (author != null) {
            articles = articleServiceForUser.getArticlesByAuthor(author);
        } else if (country != null) {
            articles = articleServiceForUser.getArticlesByCountry(country);
        } else {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(articles);
    }
}
