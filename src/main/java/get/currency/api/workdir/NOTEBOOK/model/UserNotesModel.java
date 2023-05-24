package get.currency.api.workdir.NOTEBOOK.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import get.currency.api.workdir.AUTH.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "task_list")
@AllArgsConstructor
@NoArgsConstructor
public class UserNotesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "key", nullable = false, unique = true)
    private String key;

    @Column(name = "note_status")
    private NoteStatus noteStatus;

    @Column(name = "type")
    private Availability availability;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public UserNotesModel(String subject, String description, String key, User user) {
        this.subject = subject;
        this.description = description;
        this.key = key;
        this.noteStatus = NoteStatus.NEW;
        this.user = user;
        this.availability = Availability.PUBLIC;

    }

    public NoteStatus getStatus() {
        return noteStatus;
    }
}
