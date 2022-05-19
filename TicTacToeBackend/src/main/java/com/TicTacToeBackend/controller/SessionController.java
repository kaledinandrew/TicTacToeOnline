package com.TicTacToeBackend.controller;

import com.TicTacToeBackend.model.Session;
import com.TicTacToeBackend.model.User;
import com.TicTacToeBackend.repository.*;
import com.TicTacToeBackend.service.UpdateResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final UpdateResultService updateResultService;

    @GetMapping("/all-sessions-ids")
    public List<Long> getAllSessions() {
        return sessionRepository.findAll().stream().map(Session::getSessionId).toList();
    }

    @GetMapping("")
    public Session getSessionBySessionId(@RequestParam(value = "sessionId") Long sessionId) {
        Session session = sessionRepository.findFirstBySessionId(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("Session with sessionId = " + sessionId + " not found");
        }
        return session;
    }

    @PostMapping("")
    public Session createSession(@RequestParam(value = "hostId") Long hostId) {
        User user = userRepository.findFirstByUserId(hostId);
        if (user == null) {
            throw new IllegalArgumentException("User with userId = " + hostId + " not found");
        }

        Session session = new Session();
        session.setHostId(hostId);
        session.setField("0,0,0;0,0,0;0,0,0");
        session.setIsHostTurn(true);
        session.setResult("NOT_FINISHED");
        return sessionRepository.save(session);
    }

    @PutMapping("/connect")
    public Session connectToSession(@RequestParam(value = "sessionId") Long sessionId,
                                    @RequestParam(value = "guestId") Long guestId) {
        Session session = sessionRepository.findFirstBySessionId(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("Session with sessionId = " + sessionId + " not found");
        }
        User user = userRepository.findFirstByUserId(guestId);
        if (user == null) {
            throw new IllegalArgumentException("User with userId = " + guestId + " not found");
        }

        session.setGuestId(guestId);
        return sessionRepository.save(session);
    }

    @PutMapping("/place-symbol")
    public Session placeSymbol(@RequestParam(value = "sessionId") Long sessionId,
                               @RequestParam(value = "userId") Long userId,
                               @RequestParam(value = "x") Integer x,
                               @RequestParam(value = "y") Integer y) {
        Long symbol = userRepository.findFirstByUserId(userId).getSymbol();
        Session session = sessionRepository.findFirstBySessionId(sessionId);

        if (symbol == null) {
            throw new IllegalArgumentException("User with userId = " + userId + " not found");
        }
        if (session == null) {
            throw new IllegalArgumentException("Session with sessionId = " + sessionId + " not found");
        }

        if (x < 0 || x > session.getField().size() || y < 0 || y > session.getField().get(0).size()) {
            throw new IllegalArgumentException("Cell (x = " + x + "; y = " + y + ") is invalid");
        }

        List<List<Long>> field = session.getField();
        if (field.get(x).get(y) != 0) {
            throw new IllegalArgumentException("Cell (x = " + x + "; y = " + y + ") is already occupied");
        }

        List<List<Long>> updatedField = new ArrayList<>();
        for (int i = 0; i < field.size(); i++) {
            List<Long> tmp = new ArrayList<>();
            for (int j = 0; j < field.get(i).size(); j++) {
                if (i == x && j == y) {
                    tmp.add(symbol);
                } else {
                    tmp.add(field.get(i).get(j));
                }
            }
            updatedField.add(tmp);
        }
        session.setField(updatedField);
        session.setResult(updateResultService.getCurrentResult(session, x, y));
        session.setIsHostTurn(!session.getIsHostTurn());
        return sessionRepository.save(session);
    }

}
