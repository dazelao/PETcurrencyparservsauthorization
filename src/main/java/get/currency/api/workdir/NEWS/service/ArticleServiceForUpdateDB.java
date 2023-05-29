package get.currency.api.workdir.NEWS.service;

import get.currency.api.workdir.NEWS.model.Article;
import get.currency.api.workdir.NEWS.utils.HashUtils;
import get.currency.api.workdir.NEWS.model.NewsApiResponse;
import get.currency.api.workdir.NEWS.repository.ArticleRepository;
import get.currency.api.workdir.PINGER.TelegramBot;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Data
public class ArticleServiceForUpdateDB {
    @Value("${api.key}")
    private String apiKey;
    @PersistenceContext
    private EntityManager entityManager;
    private final ArticleRepository articleRepository;
    private final TelegramBot telegramBot;


    @Autowired
    public ArticleServiceForUpdateDB(ArticleRepository articleRepository, TelegramBot telegramBot) {
        this.articleRepository = articleRepository;

        this.telegramBot = telegramBot;
    }



    @Transactional
    public List<Article> fetchAndSaveArticles(String country, String category) {
        String apiUrl = "https://newsapi.org/v2/top-headlines?country=" + country + "&category=" + category + "&apiKey=" + apiKey;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<NewsApiResponse> responseEntity = restTemplate.getForEntity(apiUrl, NewsApiResponse.class);

        HttpStatus statusCode = responseEntity.getStatusCode();
        telegramBot.sendTelegramMessage("FetchNews Статус ответа API: " + statusCode);

        if (statusCode != HttpStatus.OK) {
            telegramBot.sendTelegramMessage("FetchNews Ошибка при получении новостей: " + statusCode);
            return null;
        }

        NewsApiResponse response = responseEntity.getBody();
        List<Article> fetchedArticles = new ArrayList<>();

        if (response != null && response.getStatus().equals("ok")) {
            fetchedArticles = response.getArticles();

            for (Article article : fetchedArticles) {
                try {
                    String url = article.getUrl();
                    LocalDateTime publishedAt = article.getPublishedAt();
                    String hash = HashUtils.generateHash(url, publishedAt);
                    article.setHash(hash);
                    article.setCategory(category);
                    article.setCountry(country);

                    boolean articleExists = articleRepository.existsByHash(hash);
                    if (!articleExists) {
                        articleRepository.save(article);
                    }
                } catch (DataIntegrityViolationException e) {
                    telegramBot.sendTelegramMessage("FetchNews Ошибка при получении данных" + e);
                }
            }
        }

        return fetchedArticles;
    }

    @Transactional
    public void fetchAndSaveAllArticles() {
        telegramBot.sendTelegramMessage("Запущен метод получения данных");
        String[] countries = {"gb", "ua", "us"};
        String[] categories = {"business", "entertainment", "science", "sports", "technology"};
        long startTime = System.currentTimeMillis();
        List<Article> articles = new ArrayList<>();

        for (String country : countries) {
            for (String category : categories) {
                List<Article> fetchedArticles = fetchAndSaveArticles(country, category);
                if (fetchedArticles != null) {
                    for (Article fetchedArticle : fetchedArticles) {
                        boolean articleExists = articleRepository.existsByHash(fetchedArticle.getHash());
                        if (!articleExists) {
                            fetchedArticle.setCategory(category);
                            fetchedArticle.setCountry(country);
                            articles.add(fetchedArticle);
                        }
                    }
                }
            }
        }

        List<Article> savedArticles = articleRepository.saveAll(articles);
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        telegramBot.sendTelegramMessage("Общее время выполнения: " + totalTime + " миллисекунд");
        telegramBot.sendTelegramMessage("Новости получены");
    }





    @Transactional
    public void removeDuplicates() {

        telegramBot.sendTelegramMessage("Запуск удаления дубликатов");
        long startTime = System.currentTimeMillis();

        List<Object[]> duplicatesWithAuthorNull = entityManager.createQuery(
                        "SELECT a.title, a.publishedAt " +
                                "FROM Article a " +
                                "WHERE a.author IS NULL " +
                                "GROUP BY a.title, a.publishedAt " +
                                "HAVING COUNT(*) > 1", Object[].class)
                .getResultList();

        List<Article> duplicatesToDelete = new ArrayList<>();

        for (Object[] duplicate : duplicatesWithAuthorNull) {
            String title = (String) duplicate[0];
            LocalDateTime publishedAt = (LocalDateTime) duplicate[1];

            List<Article> articles = entityManager.createQuery(
                            "SELECT a " +
                                    "FROM Article a " +
                                    "WHERE a.author IS NULL " +
                                    "AND a.title = :title " +
                                    "AND a.publishedAt = :publishedAt", Article.class)
                    .setParameter("title", title)
                    .setParameter("publishedAt", publishedAt)
                    .getResultList();

            duplicatesToDelete.addAll(articles.subList(1, articles.size()));
        }

        List<Object[]> duplicatesWithAuthorNotNull = entityManager.createQuery(
                        "SELECT a.author, a.publishedAt " +
                                "FROM Article a " +
                                "WHERE a.author IS NOT NULL " +
                                "GROUP BY a.author, a.publishedAt " +
                                "HAVING COUNT(*) > 1", Object[].class)
                .getResultList();

        for (Object[] duplicate : duplicatesWithAuthorNotNull) {
            String author = (String) duplicate[0];
            LocalDateTime publishedAt = (LocalDateTime) duplicate[1];

            List<Article> articles = entityManager.createQuery(
                            "SELECT a " +
                                    "FROM Article a " +
                                    "WHERE a.author = :author " +
                                    "AND a.publishedAt = :publishedAt", Article.class)
                    .setParameter("author", author)
                    .setParameter("publishedAt", publishedAt)
                    .getResultList();

            duplicatesToDelete.addAll(articles.subList(1, articles.size()));
        }

        articleRepository.deleteAll(duplicatesToDelete);

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        telegramBot.sendTelegramMessage("Дубликаты удалены. Общее время выполнения: " + totalTime + " миллисекунд");
    }


}
