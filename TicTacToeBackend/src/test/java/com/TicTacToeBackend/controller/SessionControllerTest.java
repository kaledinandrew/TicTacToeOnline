package com.TicTacToeBackend.controller;

import com.TicTacToeBackend.model.Session;
import com.TicTacToeBackend.model.User;
import com.TicTacToeBackend.repository.SessionRepository;
import com.TicTacToeBackend.repository.UserRepository;
import com.TicTacToeBackend.service.UpdateResultService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SessionController.class)
@MockBeans({@MockBean(SessionRepository.class), @MockBean(UserRepository.class), @MockBean(UpdateResultService.class)})
@AutoConfigureMockMvc(addFilters = false)
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionRepository sessionRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UpdateResultService updateResultService;

    @Captor
    private ArgumentCaptor<Session> captor;

    @Test
    public void getSessionBySessionId_sessionExists() throws Exception {
        when(sessionRepository.findFirstBySessionId(anyLong())).thenReturn(createSession());
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
        String response = """
                {
                	"message": "Session with sessionId = 100 not found"
                }""";
        when(sessionRepository.findFirstBySessionId(any())).thenReturn(null);
        var resultActions = mockMvc.perform(get("/sessions?sessionId=100")
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().is4xxClientError());
        resultActions.andExpect(content().json(response));
    }

    @Test
    public void createSession_ok() throws Exception {
        when(userRepository.findFirstByUserId(any())).thenReturn(createUser());
        var resultActions = mockMvc.perform(post("/sessions?hostId=1"));
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void createSession_invalidHostId() throws Exception {
        String response = """
                {
                	"message": "User with userId = 10000 not found"
                }""";
        when(userRepository.findFirstByUserId(any())).thenReturn(null);
        var resultActions = mockMvc.perform(post("/sessions?hostId=10000")
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().is4xxClientError());
        resultActions.andExpect(content().json(response));
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
    public void connectToSession_invalidUser() throws Exception {
        String response = """
                {
                	"message": "User with userId = 1 not found"
                }""";
        when(sessionRepository.findFirstBySessionId(any())).thenReturn(createSession());
        when(userRepository.findFirstByUserId(any())).thenReturn(null);
        var resultActions = mockMvc.perform(put("/sessions/connect?sessionId=1&guestId=1")
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().is4xxClientError());
        resultActions.andExpect(content().json(response));
    }

    @Test
    public void connectToSession_invalidSession() throws Exception {
        String response = """
                {
                	"message": "Session with sessionId = 1 not found"
                }""";
        when(userRepository.findFirstByUserId(any())).thenReturn(createUser());
        when(sessionRepository.findFirstBySessionId(any())).thenReturn(null);
        var resultActions = mockMvc.perform(put("/sessions/connect?sessionId=1&guestId=1")
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().is4xxClientError());
        resultActions.andExpect(content().json(response));
    }

    @Test
    public void placeSymbol_invalidCoordinates() throws Exception {
        when(userRepository.findFirstByUserId(any())).thenReturn(createUser());
        when(sessionRepository.findFirstBySessionId(any())).thenReturn(createSession());
        var resultActions = mockMvc.perform(put("/sessions/place-symbol?sessionId=1&userId=1&x=-1&y=-1")
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().is4xxClientError());
    }

    @Test
    public void placeSymbol_ok() throws Exception {
        Session expected = createSession();
        expected.setField("0,0,0;0,1,0;0,0,0");
        expected.setIsHostTurn(false);
        when(userRepository.findFirstByUserId(any())).thenReturn(createUser());
        when(sessionRepository.findFirstBySessionId(any())).thenReturn(createSession());
        when(updateResultService.getCurrentResult(any(), anyBoolean(), anyInt(), anyInt())).thenReturn("NOT_FINISHED");
        when(sessionRepository.save(captor.capture())).thenReturn(createSession());

        var resultActions = mockMvc.perform(put("/sessions/place-symbol?sessionId=1&userId=1&x=1&y=1")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        Session result = captor.getValue();
        assertAll(
                () -> assertEquals(expected.getSessionId(), result.getSessionId()),
                () -> assertEquals(expected.getField(), result.getField()),
                () -> assertEquals(expected.getIsHostTurn(), result.getIsHostTurn())
        );
        assertEquals(result, captor.getValue());
    }


    private User createUser() {
        return new User(1L, "Mike", 1L);
    }

    private Session createSession() {
        return new Session(1L, 2L, 3L, "0,0,0;0,0,0;0,0,0", true, "NOT_FINISHED");
    }
}