package get.currency.api.workdir.NEWS.repository;

import get.currency.api.workdir.AUTH.model.User;
import get.currency.api.workdir.NEWS.model.Article;
import get.currency.api.workdir.NEWS.model.NewsStatus;
import get.currency.api.workdir.NEWS.model.UserNewsStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsStatusRepository extends JpaRepository<UserNewsStatus, Long> {

    UserNewsStatus findByHashCode(String hashCode);
    UserNewsStatus findByUserAndArticle(User user, Article article);

    List<UserNewsStatus> findByUser_IdAndStatusAndLiked(Long userId, NewsStatus status, boolean liked);

    List<UserNewsStatus> findByUser_Id(Long id);

}
