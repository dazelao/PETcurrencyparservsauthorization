package get.currency.api.workdir.NEWS.model;

import get.currency.api.workdir.AUTH.model.User;

import javax.persistence.*;

@Entity
@Table(name = "user_news_status")
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
    private String status;

    @Column(name = "liked")
    private boolean liked;

}
