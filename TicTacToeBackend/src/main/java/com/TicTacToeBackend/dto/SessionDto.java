package com.TicTacToeBackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionDto {

    private Long sessionId;

    private Long hostId;

    private Long guestId;

    // Field format: "1,2,3;4,5,6;7,8,9"
    // That means we have a matrix 3x3
    private String field;

}
