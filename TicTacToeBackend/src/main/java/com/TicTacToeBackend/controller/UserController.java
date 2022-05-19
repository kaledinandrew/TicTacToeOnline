package com.TicTacToeBackend.controller;

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
        User user = userRepository.findFirstByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("User with userId = " + userId + " not found");
        }
        return user;
    }

    @PostMapping("")
    public User createUser(@RequestParam(value = "name") String name,
                           @RequestParam(value = "symbol") long symbol) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Request body is not valid");
        }
        return userRepository.save(new User(name, symbol));
    }

}
