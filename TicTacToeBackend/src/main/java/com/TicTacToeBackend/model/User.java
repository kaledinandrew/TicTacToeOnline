package com.TicTacToeBackend.model;

import lombok.*;

import javax.persistence.*;

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

    //
    @Column(name = "symbol")
    private Long symbol;

    public User(String name, Long symbol) {
        this.name = name;
        this.symbol = symbol;
    }

}
