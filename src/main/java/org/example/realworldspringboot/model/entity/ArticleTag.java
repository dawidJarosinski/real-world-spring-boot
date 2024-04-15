package org.example.realworldspringboot.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "article_tags")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_tag_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "article_tag_article")
    private Article article;

    @ManyToOne
    @JoinColumn(name = "article_tag_tag")
    private Tag tag;

    public ArticleTag(Article article, Tag tag) {
        this.article = article;
        this.tag = tag;
    }
}
