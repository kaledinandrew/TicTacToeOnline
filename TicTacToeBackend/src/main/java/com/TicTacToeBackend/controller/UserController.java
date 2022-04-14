package com.TicTacToeBackend.controller;

import com.TicTacToeBackend.dto.*;
import com.TicTacToeBackend.model.User;
import com.TicTacToeBackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/all-users-ids")
    public List<Long> getUsers() {
        return userRepository.findAll().stream().map(User::getUserId).toList();
    }

    @GetMapping("")
    public User getUserById(@RequestParam(value = "userId") Long userId) {
        return userRepository.findFirstByUserId(userId);
    }

    @PostMapping("")
    public User createUser(@RequestBody UserDto dto) {
        return userRepository.save(dto.toUser());
    }

}
