package get.currency.api.workdir.NOTEBOOK.controller;


import get.currency.api.workdir.AUTH.model.User;
import get.currency.api.workdir.NOTEBOOK.model.Availability;
import get.currency.api.workdir.NOTEBOOK.model.NoteStatus;
import get.currency.api.workdir.NOTEBOOK.model.UserNotesModel;
import get.currency.api.workdir.NOTEBOOK.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usernotes")
public class NoteController {
    private final NoteService noteService;


    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/createtask")
    public ResponseEntity<UserNotesModel> addNewTask(
            @RequestParam("subject") String subject,
            @RequestParam("description") String description,
            @RequestHeader("Authorization") String token)
    {
        User user = noteService.getUserFromToken(token);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String encryptedKey = noteService.keyGenerator(user.getId(), subject);

        UserNotesModel addNote = new UserNotesModel(subject, description, encryptedKey, user);
        noteService.createTask(addNote);
        return ResponseEntity.ok(addNote);
    }

    @GetMapping("/getauthusernotes")
    public ResponseEntity<List<UserNotesModel>> getUserNotes(@RequestHeader("Authorization") String token) {
        User user = noteService.getUserFromToken(token);
        List<UserNotesModel> notes = noteService.getAllByUserId(user.getId());

        List<UserNotesModel> filteredNotes = notes.stream()
                .filter(note -> note.getStatus() != NoteStatus.DEL)
                .collect(Collectors.toList());

        return ResponseEntity.ok(filteredNotes);
    }

    @GetMapping("/getKey")
    public ResponseEntity<String> getKey(@RequestParam("id") Long id) {
        UserNotesModel userNotesModel = noteService.getKeyById(id);
        if (userNotesModel == null) {
            return ResponseEntity.notFound().build();
        }

        String key = userNotesModel.getKey();
        return ResponseEntity.ok(key);
    }

    @GetMapping("/getTaskByKey")
    public ResponseEntity<UserNotesModel> getTaskByKey(@RequestParam("key") String key, @RequestHeader("Authorization") String token) {
        User user = noteService.getUserFromToken(token);
        UserNotesModel userNotesModel = noteService.getTaskByKey(key);

        if (userNotesModel == null) {
            return ResponseEntity.notFound().build();
        }

        if (userNotesModel.getAvailability() == Availability.PRIVATE && !user.getId().equals(userNotesModel.getUser().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new UserNotesModel("Нет доступа", "", "", null));
        }

        return ResponseEntity.ok(userNotesModel);
    }


    @PutMapping("/updatetask")
    public ResponseEntity<UserNotesModel> updateTask(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "subject", required = false) String subject,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "noteStatus", required = false) NoteStatus noteStatus,
            @RequestParam(value = "availability", required = false) Availability availability,
            @RequestHeader("Authorization") String token)
    {
        User user = noteService.getUserFromToken(token);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserNotesModel updatedTask = noteService.updateTask(key, id, subject, description, noteStatus, availability);
        if (updatedTask != null) {
            return ResponseEntity.ok(updatedTask);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
