package com.vvt.quizengine.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter @Getter @NoArgsConstructor
public class SolutionDTO {
    private Long quizId;

    private List<ResponseDTO> responses;
}
