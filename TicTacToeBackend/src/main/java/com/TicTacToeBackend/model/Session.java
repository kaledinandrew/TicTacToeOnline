package com.TicTacToeBackend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

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

    @Column(name = "host_id")
    private Long hostId;

    @Column(name = "guest_id")
    private Long guestId;

    // Field format: "1,2,3;4,5,6;7,8,9"
    @Column(name = "field")
    private String field;

    @Column(name = "isHostTurn")
    private Boolean isHostTurn;

    // Possible values: NOT_FINISHED, HOST_WIN, GUEST_WIN, DRAW
    @Column(name = "result")
    private String result;

    public List<List<Long>> getField() {
        List<List<Long>> matrix = Arrays.stream(field.split(";"))
                .map(
                        row -> Arrays.stream(row.split(",")).map(Long::parseLong).toList()
                ).toList();
        return matrix;
    }

    public void setField(List<List<Long>> field) {
        this.field = String.join(
                ";",
                field.stream()
                        .map(
                                row -> String.join(",", row.stream().map(Object::toString).toList())
                        ).toList()
        );
    }

    public void setField(String field) {
        this.field = field;
    }
}
