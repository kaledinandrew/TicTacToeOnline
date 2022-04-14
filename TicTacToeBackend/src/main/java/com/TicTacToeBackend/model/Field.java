package com.TicTacToeBackend.model;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Field {

    private List<List<Long>> matrix;

}
