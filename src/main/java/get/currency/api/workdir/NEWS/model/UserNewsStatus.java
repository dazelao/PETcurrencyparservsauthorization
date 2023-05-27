package get.currency.api.workdir.NEWS.model;

import get.currency.api.workdir.AUTH.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table(name = "user_news_status")
@Data
@NoArgsConstructor
public class UserNewsStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private NewsStatus status;

    @Column(name = "liked")
    private boolean liked;

    @Column(name = "hash_code")
    private String hashCode;

    public UserNewsStatus(User user, Article article) {
        this.user = user;
        this.article = article;
        this.status = NewsStatus.WATCHED;
        this.liked = false;
    }

}
