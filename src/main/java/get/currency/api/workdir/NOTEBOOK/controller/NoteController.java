package get.currency.api.workdir.NOTEBOOK.controller;


import get.currency.api.workdir.AUTH.model.User;
import get.currency.api.workdir.NOTEBOOK.model.UserNotesModel;
import get.currency.api.workdir.NOTEBOOK.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @RequestParam("key") String key,
            @RequestHeader("Authorization") String token)
    {
        User user = noteService.getUserFromToken(token);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserNotesModel addNote = new UserNotesModel(subject, description, key, user);
        noteService.createTask(addNote);
        return ResponseEntity.ok(addNote);
    }

    @GetMapping("/getauthusernotes")
    public ResponseEntity<List<UserNotesModel>> getUserNotes(@RequestHeader("Authorization") String token) {
        User user = noteService.getUserFromToken(token);
        List<UserNotesModel> notes = noteService.findAllByUserId(user.getId());
        return ResponseEntity.ok(notes);
    }



}
