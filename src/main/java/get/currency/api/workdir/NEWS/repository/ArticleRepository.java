package get.currency.api.workdir.NEWS.repository;

import get.currency.api.workdir.NEWS.model.Article;
import get.currency.api.workdir.NEWS.model.NewsStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ArticleRepository extends JpaRepository<Article, Long> {
    boolean existsByHash(String hash);
    List<Article> findAllByCategory(String category);
    List<Article> findAllByAuthor(String author);
    List<Article> findAllByCountry(String country);

}
