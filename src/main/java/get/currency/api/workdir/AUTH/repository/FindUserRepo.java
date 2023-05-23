package get.currency.api.workdir.AUTH.repository;

import get.currency.api.workdir.AUTH.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FindUserRepo extends JpaRepository<User, Long> {
    User findByEmail(String username);
}
