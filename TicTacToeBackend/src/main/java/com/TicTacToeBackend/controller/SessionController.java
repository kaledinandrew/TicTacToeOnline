package com.TicTacToeBackend.controller;

import com.TicTacToeBackend.model.Field;
import com.TicTacToeBackend.model.Session;
import com.TicTacToeBackend.repository.*;
import com.TicTacToeBackend.dto.*;
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

    @GetMapping("/all-sessions-ids")
    public List<Long> getAllSessions() {
        return sessionRepository.findAll().stream().map(Session::getSessionId).toList();
    }

    @GetMapping("")
    public Session getSessionBySessionId(@RequestParam(value = "sessionId") Long sessionId) {
        return sessionRepository.findFirstBySessionId(sessionId);
    }

    @PostMapping("")
    public Session createSession(@RequestParam(value = "hostId") Long hostId) {
        Session session = new Session();
//        session.setHost(userRepository.getById(hostId));
        session.setHostId(hostId);
        session.setField("0,0,0;0,0,0;0,0,0");
        session.setIsHostTurn(true);
        return sessionRepository.save(session);
    }

    @PutMapping("/connect")
    public Session connectToSession(@RequestParam(value = "sessionId") Long sessionId,
                                    @RequestParam(value = "guestId") Long guestId) {
        Session session = sessionRepository.findFirstBySessionId(sessionId);
//        session.setGuest(userRepository.findFirstByUserId(guestId));
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

        List<List<Long>> field = session.getField();
        if (field.get(x).get(y) != 0) {
            return null;
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
//        field.get(x).set(y, symbol);
        session.setField(updatedField);
        session.setIsHostTurn(!session.getIsHostTurn());
        return sessionRepository.save(session);
    }

}
