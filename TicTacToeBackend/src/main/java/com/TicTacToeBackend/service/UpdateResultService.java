package com.TicTacToeBackend.service;

import com.TicTacToeBackend.model.Session;

import java.util.List;

import static java.lang.Math.min;

public class UpdateResultService {

    private enum ResultValues {
        NOT_FINISHED,
        HOST_WIN,
        GUEST_WIN,
        DRAW
    }

    public String getCurrentResult(Session session, int x, int y) {
        List<List<Long>> field = session.getField();
        long newSymbol = field.get(x).get(y);
        int vertical = 0, horizontal = 0, mainDiagonal = 0, subDiagonal = 0;
        int n = field.size(), m = field.get(0).size();

        for (int i = 0; i < n; i++) {
            if (field.get(i).get(y) == newSymbol) vertical++;
        }
        for (int j = 0; j < m; j++) {
            if (field.get(x).get(j) == newSymbol) horizontal++;
        }
        if (n > m && x - y >= 0 && x + y < m) {
            for (int i = 0; i < m; i++) {
                if (field.get(x - y + i).get(i) == newSymbol) mainDiagonal++;
            }
        } else if (n <= m && y - x >= 0 && x + y < n) {
            for (int i = 0; i < n; i++) {
                if (field.get(i).get(y - x + i) == newSymbol) subDiagonal++;
            }
        }

        boolean isWin = (vertical == n || horizontal == m || mainDiagonal == min(n, m) || subDiagonal == min(n, m));

        if (isWin && session.getIsHostTurn()) {
            return ResultValues.HOST_WIN.name();
        } else if (!session.getIsHostTurn()) {
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
