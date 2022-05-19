package com.TicTacToeBackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UpdateResultServiceTest {

    private enum ResultValues {
        NOT_FINISHED,
        HOST_WIN,
        GUEST_WIN,
        DRAW
    }

    @InjectMocks
    private UpdateResultService updateResultService;

    @Test
    public void test() {
        // given
        List<List<Long>> field = List.of(List.of(1L, 1L, 1L), List.of(0L, 0L, 0L), List.of(0L, 0L, 0L));

        // when
        String result = updateResultService.getCurrentResult(field, true, 0, 1);

        // then
        assertEquals(ResultValues.HOST_WIN.name(), result);
    }

}