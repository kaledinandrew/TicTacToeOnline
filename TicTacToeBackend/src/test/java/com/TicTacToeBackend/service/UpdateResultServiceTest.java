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
    public void testHorizontal() {
        // given
        List<List<Long>> field = List.of(
                List.of(1L, 1L, 1L),
                List.of(0L, 0L, 0L),
                List.of(0L, 0L, 0L)
        );

        // when
        String result = updateResultService.getCurrentResult(field, true, 0, 1);

        // then
        assertEquals(ResultValues.HOST_WIN.name(), result);
    }

    @Test
    public void testVertical() {
        // given
        List<List<Long>> field = List.of(
                List.of(1L, 0L, 1L),
                List.of(1L, 0L, 0L),
                List.of(1L, 1L, 0L)
        );

        // when
        String result = updateResultService.getCurrentResult(field, true, 1, 0);

        // then
        assertEquals(ResultValues.HOST_WIN.name(), result);
    }

    @Test
    public void testGuestWin() {
        // given
        List<List<Long>> field = List.of(
                List.of(1L, 0L, 1L),
                List.of(1L, 0L, 0L),
                List.of(1L, 1L, 0L)
        );

        // when
        String result = updateResultService.getCurrentResult(field, false, 1, 0);

        // then
        assertEquals(ResultValues.GUEST_WIN.name(), result);
    }

    @Test
    public void testNotFinished() {
        // given
        List<List<Long>> field = List.of(
                List.of(1L, 1L, 0L),
                List.of(0L, 1L, 0L),
                List.of(0L, 0L, 1L),
                List.of(0L, 0L, 1L)
        );

        // when
        String result = updateResultService.getCurrentResult(field, true, 2, 0);

        // then
        assertEquals(ResultValues.NOT_FINISHED.name(), result);
    }

    @Test
    public void testDraw() {
        // given
        List<List<Long>> field = List.of(
                List.of(1L, 0L, 1L),
                List.of(0L, 1L, 1L)
        );

        // when
        String result = updateResultService.getCurrentResult(field, true, 1, 0);

        // then
        assertEquals(ResultValues.NOT_FINISHED.name(), result);
    }

}