package com.TicTacToeBackend.controller;

import com.TicTacToeBackend.model.Session;
import com.TicTacToeBackend.model.User;
import com.TicTacToeBackend.repository.SessionRepository;
import com.TicTacToeBackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private SessionController sessionController;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void getSessionBySessionId_sessionExists() throws Exception {
        when(sessionRepository.findFirstBySessionId(any())).thenReturn(createSession());
        var resultActions = mockMvc.perform(get("/sessions?sessionId=1")
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void getSessionBySessionId_invalidSessionId() throws Exception {
        var resultActions = mockMvc.perform(get("/sessions?sessionId=asdf")
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().is4xxClientError());
    }

    @Test
    public void getSessionBySessionId_noSessionExists() throws Exception {
        when(sessionRepository.findFirstBySessionId(any())).thenReturn(null);
        var resultActions = mockMvc.perform(get("/sessions?sessionId=100")
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().is4xxClientError());
    }

    @Test
    public void createSession_ok() throws Exception {
        when(userRepository.findFirstByUserId(any())).thenReturn(createUser());
        var resultActions = mockMvc.perform(post("/sessions?hostId=1"));
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void createSession_invalidHostId() throws Exception {
        when(userRepository.findFirstByUserId(any())).thenReturn(null);
        var resultActions = mockMvc.perform(get("/sessions?sessionId=100")
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().is4xxClientError());
    }

    @Test
    public void connectToSession_ok() throws Exception {
        when(userRepository.findFirstByUserId(any())).thenReturn(createUser());
        when(sessionRepository.findFirstBySessionId(any())).thenReturn(createSession());
        var resultActions = mockMvc.perform(put("/sessions/connect?sessionId=1&guestId=1")
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void placeSymbol_invalidCoordinates() throws Exception {
        when(userRepository.findFirstByUserId(any())).thenReturn(createUser());
        when(sessionRepository.findFirstBySessionId(any())).thenReturn(createSession());
        var resultActions = mockMvc.perform(put("/sessions/place-symbol?sessionId=1&userId=1&x=-1&y=-1")
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().is4xxClientError());
    }


    private User createUser() {
        return new User(1L, "Mike", 1L);
    }

    private Session createSession() {
        return new Session(1L, 2L, 3L, "", true);
    }
}