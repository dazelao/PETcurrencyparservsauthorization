package get.currency.api.workdir.NOTEBOOK.service;

import get.currency.api.workdir.AUTH.model.User;
import get.currency.api.workdir.NOTEBOOK.model.Availability;
import get.currency.api.workdir.NOTEBOOK.model.NoteStatus;
import get.currency.api.workdir.NOTEBOOK.repository.FindUserRepo;
import get.currency.api.workdir.AUTH.security.JwtTokenProvider;
import get.currency.api.workdir.NOTEBOOK.model.UserNotesModel;
import get.currency.api.workdir.NOTEBOOK.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
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

    public String keyGenerator(Long userId, String subject) {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String key = currentTime.format(formatter) + "_" + userId + "_" + subject;

        String encodedKey = Base64.getUrlEncoder().encodeToString(key.getBytes());
        return encodedKey.replace('/', '-');
    }

    public UserNotesModel getKeyById (Long id){
        return noteRepository.findKeyById(id);
    }

    public UserNotesModel getTaskByKey (String key){
        return noteRepository.findByKey(key);
    }

    public UserNotesModel updateTask(String key, Long id, String subject, String description, NoteStatus noteStatus, Availability availability) {
        UserNotesModel existingTask;

        if (key != null) {
            existingTask = noteRepository.findByKey(key);
        } else if (id != null) {
            existingTask = noteRepository.findById(id).orElse(null);
        } else {
            return null;
        }

        if (existingTask != null) {
            if (subject != null) {
                existingTask.setSubject(subject);
            }
            if (description != null) {
                existingTask.setDescription(description);
            }
            if (noteStatus != null) {
                existingTask.setNoteStatus(noteStatus);
            }
            if (availability != null) {
                existingTask.setAvailability(availability);
            }

            return noteRepository.save(existingTask);
        }

        return null;
    }
}
