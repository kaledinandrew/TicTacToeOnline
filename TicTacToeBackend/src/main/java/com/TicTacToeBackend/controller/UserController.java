package com.TicTacToeBackend.controller;

import com.TicTacToeBackend.dto.*;
import com.TicTacToeBackend.model.User;
import com.TicTacToeBackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
        User user = userRepository.findFirstByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("User with userId = " + userId + " not found");
        }
        return user;
    }

    @PostMapping("")
    public User createUser(@Valid @RequestBody UserDto dto) {
        if (dto == null || dto.getName() == null || dto.getSymbol() == null || dto.getName().isEmpty()) {
            throw new IllegalArgumentException("Request body is not valid");
        }
        return userRepository.save(dto.toUser());
    }

}
