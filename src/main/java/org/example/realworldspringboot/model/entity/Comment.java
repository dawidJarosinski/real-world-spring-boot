package org.example.realworldspringboot.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer id;

    @Column(name = "comment_content")
    private String content;
    @ManyToOne
    @JoinColumn(name = "comment_author")
    private User author;
    @ManyToOne
    @JoinColumn(name = "comment_article")
    private Article article;
}
