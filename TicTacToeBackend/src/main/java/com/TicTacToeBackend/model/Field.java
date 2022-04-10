package com.TicTacToeBackend.model;

import lombok.Data;

import java.util.List;

@Data
public class Field {

    private Long width;

    private Long height;

    private List<List<Long>> matrix;

}
