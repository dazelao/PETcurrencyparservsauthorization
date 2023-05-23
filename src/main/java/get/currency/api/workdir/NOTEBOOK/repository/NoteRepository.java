package get.currency.api.workdir.NOTEBOOK.repository;

import get.currency.api.workdir.NOTEBOOK.model.UserNotesModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<UserNotesModel, Long> {
    List<UserNotesModel> findAllByUserId(Long id);
}
