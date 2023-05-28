package get.currency.api.workdir.NEWS.controller;


import get.currency.api.workdir.AUTH.model.User;
import get.currency.api.workdir.NEWS.model.Article;
import get.currency.api.workdir.NEWS.repository.ArticleRepository;
import get.currency.api.workdir.NOTEBOOK.service.NoteService;
import get.currency.api.workdir.NEWS.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/prefer")
public class LikeController {

    public final LikeService likeService;
    public final NoteService noteService;

    public final ArticleRepository articleRepository;

    @Autowired
    public LikeController(LikeService likeService, NoteService noteService, ArticleRepository articleRepository) {
        this.likeService = likeService;
        this.noteService = noteService;
        this.articleRepository = articleRepository;
    }

    @GetMapping("/savedlengs")
    public ResponseEntity<Integer> getSavedUserArticleCount(@RequestHeader("Authorization") String token) {
        User user = noteService.getUserFromToken(token);
        List<Long> savedArticleIds = likeService.getAllSavedArticleIds(user.getId());
        int count = savedArticleIds.size();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/likedlengs")
    public ResponseEntity<Integer> getLikedUserArticleCount(@RequestHeader("Authorization") String token) {
        User user = noteService.getUserFromToken(token);
        List<Long> likedArticleIds = likeService.getAllLikedArticleIds(user.getId());
        int count = likedArticleIds.size();
        return ResponseEntity.ok(count);
    }


    @GetMapping("/liked-articles")
    public ResponseEntity<List<Article>> getLikedArticles(@RequestHeader("Authorization") String token) {
        User user = noteService.getUserFromToken(token);
        List<Long> likedArticleIds = likeService.getAllLikedArticleIds(user.getId());
        List<Article> likedArticles = articleRepository.findAllByIdIn(likedArticleIds);
        return ResponseEntity.ok(likedArticles);
    }

    @GetMapping("/saved-articles")
    public ResponseEntity<List<Article>> getSavedArticles(@RequestHeader("Authorization") String token) {
        User user = noteService.getUserFromToken(token);
        List<Long> savedArticleIds = likeService.getAllSavedArticleIds(user.getId());
        List<Article> savedArticles = articleRepository.findAllByIdIn(savedArticleIds);
        return ResponseEntity.ok(savedArticles);
    }
}
