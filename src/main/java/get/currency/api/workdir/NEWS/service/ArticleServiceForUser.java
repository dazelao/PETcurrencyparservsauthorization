package get.currency.api.workdir.NEWS.service;

import get.currency.api.workdir.NEWS.model.Article;
import get.currency.api.workdir.NEWS.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleServiceForUser {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceForUser(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Optional<Article> getByid(Long id){
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
}
