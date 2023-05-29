package get.currency.api.workdir.NEWS.model;

import javax.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

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


    @Column
    private String category;

    @Column
    private String country;
}
