package get.currency.api.workdir.NEWS.repository;

import get.currency.api.workdir.NEWS.model.NewsStatus;
import get.currency.api.workdir.NEWS.model.UserNewsStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreferRepo extends JpaRepository<UserNewsStatus, Long> {
    List<UserNewsStatus> findAllByUser_IdAndLiked(Long userId, boolean liked);
    List<UserNewsStatus> findAllByUser_IdAndStatus(Long userId, NewsStatus status);
}
