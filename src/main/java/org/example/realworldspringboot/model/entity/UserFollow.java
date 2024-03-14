package org.example.realworldspringboot.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_follows")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFollow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_follow_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_follow_follower")
    private User follower;

    @ManyToOne
    @JoinColumn(name = "user_follow_followed")
    private User followed;
}
