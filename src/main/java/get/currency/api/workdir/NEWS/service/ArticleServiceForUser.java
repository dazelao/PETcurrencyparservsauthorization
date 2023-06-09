package get.currency.api.workdir.NEWS.service;

import get.currency.api.workdir.AUTH.model.User;
import get.currency.api.workdir.AUTH.repository.UserRepository;
import get.currency.api.workdir.AUTH.security.JwtTokenProvider;
import get.currency.api.workdir.NEWS.model.Article;
import get.currency.api.workdir.NEWS.model.NewsStatus;
import get.currency.api.workdir.NEWS.model.UserNewsStatus;
import get.currency.api.workdir.NEWS.repository.ArticleRepository;
import get.currency.api.workdir.NEWS.repository.NewsStatusRepository;
import get.currency.api.workdir.NOTEBOOK.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ArticleServiceForUser {

    private final ArticleRepository articleRepository;
    private final NoteService noteService;
    private final NewsStatusRepository newsStatusRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    @Autowired
    public ArticleServiceForUser(ArticleRepository articleRepository, NoteService noteService, NewsStatusRepository newsStatusRepository, JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.noteService = noteService;
        this.newsStatusRepository = newsStatusRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    public Optional<Article> getByid(Long id) {
        return articleRepository.findById(id);
    }

    public List<Article> getArticlesByCategory(String category) {
        return articleRepository.findAllByCategory(category);
    }

    public List<Article> getArticlesByAuthor(String author) {
        return articleRepository.findAllByAuthor(author);
    }

    public List<Article> getArticlesByCountry(String country) {
        return articleRepository.findAllByCountry(country);
    }

    public void updateUserNewsStatus(String token, Long articleId) {
        User user = noteService.getUserFromToken(token);
        Optional<Article> article = articleRepository.findById(articleId);

        if (user != null && article.isPresent()) {
            UserNewsStatus newsStatus = new UserNewsStatus(user, article.get());


            String hashCode = generateHashCode(newsStatus);
            UserNewsStatus existingStatus = newsStatusRepository.findByHashCode(hashCode);

            if (existingStatus == null) {
                newsStatus.setHashCode(hashCode);
                newsStatusRepository.save(newsStatus);
            }
        }
    }
    public void updateUserNewsStatus(String token, Long articleId, NewsStatus status, Boolean liked) {
        User user = noteService.getUserFromToken(token);
        Optional<Article> article = articleRepository.findById(articleId);

        if (user != null && article.isPresent()) {
            UserNewsStatus newsStatus = newsStatusRepository.findByUserAndArticle(user, article.get());

            if (newsStatus != null) {
                if (status != null) {
                    newsStatus.setStatus(status);
                }

                if (liked != null) {
                    newsStatus.setLiked(liked);
                }

                String hashCode = generateHashCode(newsStatus);
                newsStatus.setHashCode(hashCode);

                newsStatusRepository.save(newsStatus);
            }
        }
    }

    private String generateHashCode(UserNewsStatus newsStatus) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String data = newsStatus.getUser().getId() + "-" + newsStatus.getArticle().getId() + "-" + newsStatus.isLiked();
            byte[] hashBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hashBuilder = new StringBuilder();

            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hashBuilder.append('0');
                }
                hashBuilder.append(hex);
            }

            return hashBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Article> getLatestNewsByCategory(String category) {
        return articleRepository.findTop10ByCategoryOrderByPublishedAtDesc(category);
    }

    public List<Article> getLatestNewsByCategoryAndCountry(String category, String country) {
        return articleRepository.findTop10ByCategoryAndCountryOrderByPublishedAtDesc(category, country);
    }

    public Page<Article> getNewsByCategoryWithOffset(String category, int offset, int count) {
        Pageable pageable = PageRequest.of(offset, count);
        return articleRepository.findByCategoryOrderByPublishedAtDesc(category, pageable);
    }

    public Page<Article> getNewsByCategoryAndCountryWithOffset(String category, int offset, int count, String country) {
        Pageable pageable = PageRequest.of(offset, count);
        return articleRepository.findByCategoryAndCountryOrderByPublishedAtDesc(category, country, pageable);
    }

    public List<Article> getSavedOrLikedArticlesByUserId(Long userId, boolean saved, boolean liked) {
        List<Article> articles = new ArrayList<>();

        List<UserNewsStatus> userNewsStatusList = newsStatusRepository.findByUser_IdAndStatusAndLiked(userId, NewsStatus.SAVED, liked);
        for (UserNewsStatus userNewsStatus : userNewsStatusList) {
            articles.add(userNewsStatus.getArticle());
        }

        return articles;
    }

   public List<UserNewsStatus> getById(Long id){
        return newsStatusRepository.findByUser_Id(id);
   }

}
