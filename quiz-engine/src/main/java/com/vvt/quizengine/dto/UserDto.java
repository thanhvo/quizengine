package com.vvt.quizengine.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @NoArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String password;

    public UserDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
