package com.TicTacToeBackend.service;

import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.Math.min;

@Service
public class UpdateResultService {

    private enum ResultValues {
        NOT_FINISHED,
        HOST_WIN,
        GUEST_WIN,
        DRAW
    }

    public String getCurrentResult(List<List<Long>> field, boolean isHostTurn, int x, int y) {
        long newSymbol = field.get(x).get(y);
        int vertical = 0, horizontal = 0, mainDiagonal = 0, subDiagonal = 0;
        int n = field.size(), m = field.get(0).size();

        for (int i = 0; i < n; i++) {
            if (field.get(i).get(y) == newSymbol) vertical++;
            if (n == 3 && m == 3 && (x + y) % 2 == 0) {
                if (field.get(i).get(i) == newSymbol) mainDiagonal++;
                if (field.get(n - i - 1).get(m - i - 1) == newSymbol) subDiagonal++;
            }
        }
        for (int j = 0; j < m; j++) {
            if (field.get(x).get(j) == newSymbol) horizontal++;
        }

        boolean isWin = (vertical == n || horizontal == m || mainDiagonal == min(n, m) || subDiagonal == min(n, m));

        if (isWin && isHostTurn) {
            return ResultValues.HOST_WIN.name();
        } else if (isWin) {
            return ResultValues.GUEST_WIN.name();
        } else if (isFieldFilled(field)) {
            return ResultValues.DRAW.name();
        } else {
            return ResultValues.NOT_FINISHED.name();
        }
    }

    private boolean isFieldFilled(List<List<Long>> field) {
        for (List<Long> longs : field) {
            for (int j = 0; j < field.get(0).size(); j++) {
                if (longs.get(j) == 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
