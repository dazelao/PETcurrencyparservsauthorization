package get.currency.api.workdir.NEWS.repository;

import get.currency.api.workdir.NEWS.model.Article;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;


public interface ArticleRepository extends JpaRepository<Article, Long> {
    boolean existsByHash(String hash);
    List<Article> findAllByCategory(String category);
    List<Article> findAllByAuthor(String author);
    List<Article> findAllByCountry(String country);
    List<Article> findTop10ByCategoryOrderByPublishedAtDesc(String category);
    Page<Article> findByCategoryOrderByPublishedAtDesc(String category, Pageable pageable);
    List<Article> findTop10ByCategoryAndCountryOrderByPublishedAtDesc(String category, String country);
    Page<Article> findByCategoryAndCountryOrderByPublishedAtDesc(String category, String country, Pageable pageable);
    List<Article> findAllByIdIn(List<Long> articleIds);

}
