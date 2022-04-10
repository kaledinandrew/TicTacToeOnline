package com.TicTacToeBackend.controller;

import com.TicTacToeBackend.dto.SessionDto;
import com.TicTacToeBackend.model.Session;
import com.TicTacToeBackend.repository.SessionRepository;
import com.TicTacToeBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    @GetMapping("")
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    @PostMapping("")
    public Session createSession(SessionDto dto) {
        Session session = new Session();
        session.setSessionId(dto.getSessionId());
        session.setHost(userRepository.getById(dto.getHostId()));
        session.setGuest(userRepository.getById(dto.getGuestId()));
        session.setField(dto.getField());
        return sessionRepository.save(session);
    }

}
