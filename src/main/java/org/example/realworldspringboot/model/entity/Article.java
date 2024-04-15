package org.example.realworldspringboot.model.entity;


import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "articles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Integer id;

    @Column(name = "article_slug")
    private String slug;

    @Column(name = "article_title", unique = true)
    private String title;

    @Column(name = "article_description")
    private String  description;

    @Column(name = "article_body")
    private String body;

    @Column(name = "article_created_at")
    private LocalDateTime createdAt;

    @Column(name = "article_update_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "article_author")
    private User author;

    @OneToMany(targetEntity = Comment.class, mappedBy = "article", cascade = CascadeType.ALL)
    private List<Comment> comments;

    public Article(String title, String description, String body, User author) {
        this.title = title;
        this.description = description;
        this.body = body;
        this.author = author;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.slug = titleToSlug(title);
    }

    public static String titleToSlug(String title) {
        return title.toLowerCase().replaceAll("\\s+", "-");
    }

}
