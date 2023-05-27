package get.currency.api.workdir.NEWS.controller;

import get.currency.api.workdir.AUTH.model.User;
import get.currency.api.workdir.AUTH.security.JwtTokenProvider;
import get.currency.api.workdir.NEWS.model.Article;
import get.currency.api.workdir.NEWS.model.NewsStatus;
import get.currency.api.workdir.NEWS.service.ArticleServiceForUser;
import get.currency.api.workdir.NOTEBOOK.repository.FindUserRepo;
import get.currency.api.workdir.NOTEBOOK.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/news")
public class ArticleController {

    private final ArticleServiceForUser articleServiceForUser;
    private final JwtTokenProvider jwtTokenProvider;
    private final FindUserRepo findUserRepo;
    private final NoteService noteService;

    public ArticleController(ArticleServiceForUser articleServiceForUser, JwtTokenProvider jwtTokenProvider, FindUserRepo findUserRepo, NoteService noteService) {
        this.articleServiceForUser = articleServiceForUser;
        this.jwtTokenProvider = jwtTokenProvider;
        this.findUserRepo = findUserRepo;

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

}





