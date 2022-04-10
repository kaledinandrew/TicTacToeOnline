package com.TicTacToeBackend.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "host")
    private Set<Session> hostingSessions;

    @OneToMany(mappedBy = "guest")
    private Set<Session> guestingSessions;

    public User(Long userId, String name) {
        this.userId = userId;
        this.name = name;
        this.hostingSessions = new HashSet<>();
        this.guestingSessions = new HashSet<>();
    }

}
