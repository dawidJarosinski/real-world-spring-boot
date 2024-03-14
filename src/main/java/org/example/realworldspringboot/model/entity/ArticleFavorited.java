package org.example.realworldspringboot.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "article_favorites")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleFavorited {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_favorite_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "article_favorite_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "article_favorite_article")
    private Article article;
}
