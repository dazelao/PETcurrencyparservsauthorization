package get.currency.api.workdir.NEWS.controller;

import get.currency.api.workdir.AUTH.model.User;
import get.currency.api.workdir.AUTH.security.JwtTokenProvider;
import get.currency.api.workdir.NEWS.model.Article;
import get.currency.api.workdir.NEWS.model.NewsStatus;
import get.currency.api.workdir.NEWS.model.UserNewsStatus;
import get.currency.api.workdir.NEWS.service.ArticleServiceForUser;
import get.currency.api.workdir.NOTEBOOK.repository.FindUserRepo;
import get.currency.api.workdir.NOTEBOOK.service.NoteService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v2/news")
public class ArticleController {

    private final ArticleServiceForUser articleServiceForUser;
    private final NoteService noteService;


    public ArticleController(ArticleServiceForUser articleServiceForUser,NoteService noteService) {
        this.articleServiceForUser = articleServiceForUser;
        this.noteService = noteService;
    }

    @GetMapping("/id")
    public ResponseEntity<Optional<Article>> getById(@RequestParam("id") Long id,
                                                     @RequestHeader("Authorization") String token)
    {
        Optional<Article> news = articleServiceForUser.getByid(id);
        articleServiceForUser.updateUserNewsStatus(token, id);
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

    @PutMapping("/edit/{articleId}")
    public ResponseEntity<String> updateUserNewsStatus(@RequestHeader("Authorization") String token,
                                                       @PathVariable Long articleId,
                                                       @RequestParam(required = false) NewsStatus status,
                                                       @RequestParam(required = false) Boolean liked) {
        User user = noteService.getUserFromToken(token);
        Optional<Article> article = articleServiceForUser.getByid(articleId);

        if (user != null && article.isPresent()) {
            if (status != null || liked != null) {
                articleServiceForUser.updateUserNewsStatus(token, articleId, status, liked);
                return ResponseEntity.ok("UserNewsStatus updated successfully.");
            } else {
                return ResponseEntity.badRequest().body("No update parameters provided.");
            }
        } else {
            return ResponseEntity.badRequest().body("Invalid user or article.");
        }
    }

    @GetMapping("/set/{category}")
    public List<Article> getLatestNewsByCategory(@PathVariable String category,
                                                 @RequestParam(required = false) String country) {
        if (country != null) {
            return articleServiceForUser.getLatestNewsByCategoryAndCountry(category, country);
        } else {
            return articleServiceForUser.getLatestNewsByCategory(category);
        }
    }

    @GetMapping("/set/{category}/{offset}/{count}")
    public Page<Article> getNewsByCategoryWithOffset(@PathVariable String category,
                                                     @PathVariable int offset,
                                                     @PathVariable int count,
                                                     @RequestParam(required = false) String country) {
        if (country != null) {
            return articleServiceForUser.getNewsByCategoryAndCountryWithOffset(category, offset, count, country);
        } else {
            return articleServiceForUser.getNewsByCategoryWithOffset(category, offset, count);
        }
    }

    @GetMapping("/saved")
    public List<Article> getSavedArticles(@RequestHeader("Authorization") String token) {
        User user = noteService.getUserFromToken(token);
        return articleServiceForUser.getSavedOrLikedArticlesByUserId(user.getId(), true, false);
    }

    @GetMapping("/liked")
    public List<Article> getLikedArticles(@RequestHeader("Authorization") String token) {
        User user = noteService.getUserFromToken(token);
        return articleServiceForUser.getSavedOrLikedArticlesByUserId(user.getId(), false, true);
    }

    @GetMapping("/saved-liked")
    public List<Article> getSavedAndLikedArticles(@RequestHeader("Authorization") String token) {
        User user = noteService.getUserFromToken(token);
        return articleServiceForUser.getSavedOrLikedArticlesByUserId(user.getId(), true, true);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<UserNewsStatus>> getUserNewsStatus(@PathVariable("id") Long id) {
        List<UserNewsStatus> userNewsStatusList = articleServiceForUser.getById(id);
        return ResponseEntity.ok(userNewsStatusList);
    }

//    @GetMapping("/liked/{userId}")
//    public ResponseEntity<List<Article>> getArticlesByUserLiked(@PathVariable("userId") Long userId) {
//        List<Article> articles = articleServiceForUser.getArticlesByUserLiked(userId);
//        return ResponseEntity.ok(articles);
//    }

//    @GetMapping("/saved")
//    public ResponseEntity<List<Article>> getArticlesByStatusSaved() {
//        List<Article> articles = articleServiceForUser.getArticlesByStatusSaved();
//        return ResponseEntity.ok(articles);
//    }

}





