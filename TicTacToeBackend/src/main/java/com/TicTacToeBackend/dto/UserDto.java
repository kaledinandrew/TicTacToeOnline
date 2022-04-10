package com.TicTacToeBackend.dto;

import com.TicTacToeBackend.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    private Long id;

    private String name;

    public User toUser() {
        return new User(id, name);
    }
}
