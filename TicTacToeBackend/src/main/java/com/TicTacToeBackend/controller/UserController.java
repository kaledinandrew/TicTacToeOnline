package com.TicTacToeBackend.controller;

import com.TicTacToeBackend.dto.UserDto;
import com.TicTacToeBackend.model.User;
import com.TicTacToeBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PostMapping("")
    public User createUser(UserDto dto) {
        return userRepository.save(dto.toUser());
    }

}
