package get.currency.api.workdir.NEWS.service;


import get.currency.api.workdir.NEWS.model.Article;
import get.currency.api.workdir.NEWS.model.NewsStatus;
import get.currency.api.workdir.NEWS.model.UserNewsStatus;
import get.currency.api.workdir.NEWS.repository.ArticleRepository;
import get.currency.api.workdir.NEWS.repository.PreferRepo;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class LikeService {

    private final PreferRepo preferRepo;
    private final ArticleRepository articleRepo;

    public LikeService(PreferRepo preferRepo, ArticleRepository articleRepo) {
        this.preferRepo = preferRepo;
        this.articleRepo = articleRepo;
    }


    public List<Long> getAllSavedArticleIds(Long userId) {
        List<UserNewsStatus> savedNewsStatusList = preferRepo.findAllByUser_IdAndStatus(userId, NewsStatus.SAVED);
        return extractArticleIds(savedNewsStatusList);
    }

    public List<Long> getAllLikedArticleIds(Long userId) {
        List<UserNewsStatus> likedNewsStatusList = preferRepo.findAllByUser_IdAndLiked(userId, true);
        return extractArticleIds(likedNewsStatusList);
    }

    private List<Long> extractArticleIds(List<UserNewsStatus> newsStatusList) {
        List<Long> articleIds = new ArrayList<>();
        for (UserNewsStatus newsStatus : newsStatusList) {
            articleIds.add(newsStatus.getArticle().getId());
        }
        return articleIds;
    }

    public List<Article> getLikedArticles(List<Long> likedArticleIds) {
        return articleRepo.findAllByIdIn(likedArticleIds);
    }

    public List<Article> getSavedArticles(List<Long> savedArticleIds) {
        return articleRepo.findAllByIdIn(savedArticleIds);
    }
}
