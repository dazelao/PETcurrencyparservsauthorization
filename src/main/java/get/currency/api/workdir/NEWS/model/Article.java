package get.currency.api.workdir.NEWS.model;

import javax.persistence.*;

import get.currency.api.workdir.AUTH.model.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "news")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String author;

    @Column(unique = true)
    private String title;

    @Column
    private String description;

    @Column
    private String url;

    @Column
    private String urlToImage;

    @Column
    private LocalDateTime publishedAt;

    @Column
    private String content;

    @Column
    private String hash;

//    @ManyToMany(mappedBy = "articles")
//    private List<User> users;

    @Column
    private String category;

    @Column
    private String country;
}
