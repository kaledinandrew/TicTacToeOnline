package com.TicTacToeBackend.dto;

import com.TicTacToeBackend.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    @NotEmpty
    private String name;

    @NotNull
    private Long symbol;

    public User toUser() {
        return new User(name, symbol);
    }
}
