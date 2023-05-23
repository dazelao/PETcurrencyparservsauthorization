package get.currency.api.workdir.NOTEBOOK.service;

import get.currency.api.workdir.AUTH.model.User;
import get.currency.api.workdir.AUTH.repository.FindUserRepo;
import get.currency.api.workdir.AUTH.security.JwtTokenProvider;
import get.currency.api.workdir.NOTEBOOK.model.UserNotesModel;
import get.currency.api.workdir.NOTEBOOK.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final FindUserRepo findUserRepo;

    @Autowired
    public NoteService(NoteRepository noteRepository, JwtTokenProvider jwtTokenProvider, FindUserRepo findUserRepo) {
        this.noteRepository = noteRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.findUserRepo = findUserRepo;
    }

    public void createTask(UserNotesModel userNotesModel){
        noteRepository.save(userNotesModel);
    }

    public List<UserNotesModel> findAllByUserId(Long id){
        return noteRepository.findAllByUserId(id);
    }

    public User getUserFromToken(String token) {
        String username = jwtTokenProvider.getUsernameFromToken(token);
        return findUserRepo.findByEmail(username);
    }
}
