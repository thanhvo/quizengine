package com.vvt.quizengine.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter @Getter @NoArgsConstructor
public class SolutionDTO {
    private Long quizId;

    private List<ResponseDTO> responses;

    public SolutionDTO(Long quizId) {
        this.quizId = quizId;
    }

    public void addResponse(ResponseDTO response) {
        if (responses == null) responses = new ArrayList<ResponseDTO>();
        responses.add(response);
    }
}
