package com.TicTacToeBackend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long sessionId;

    @ManyToOne // owner
    @JoinColumn(name = "host_id", nullable = false)
    private User host;

    @ManyToOne
    @JoinColumn(name = "guest_id", nullable = false)
    private User guest;

    // Field format: "1,2,3;4,5,6;7,8,9"
    @Column(name = "field")
    private String field;

    public Field getField() {
        String[] params = field.split(";");
        Field field = new Field();

        field.setHeight(Long.valueOf(params[0]));
        field.setWidth(Long.valueOf(params[1]));

        List<List<Long>> matrix = new ArrayList<>();
        for (int i = 0; i < field.getHeight(); i++) {
            matrix.add(
                    Arrays.stream(params[i + 2].split(","))
                            .map(Long::parseLong)
                            .collect(Collectors.toList())
            );
        }
        field.setMatrix(matrix);
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setField(Field field) {
        StringBuilder builder = new StringBuilder();
        builder.append(field.getHeight() + ';');
        builder.append(field.getWidth() + ';');
        for (var row : field.getMatrix()) {
            builder.append(rowToString(row)).append(';');
        }
        this.field = builder.toString();
    }

    private String rowToString(List<Long> row) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < row.size(); i++) {
            builder.append(row.get(i));
            if (i < row.size() - 1) {
                builder.append(',');
            }
        }
        return builder.toString();
    }
}
