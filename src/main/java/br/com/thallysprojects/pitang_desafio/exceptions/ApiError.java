package br.com.thallysprojects.pitang_desafio.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApiError {

    private String message;
    private Integer errorCode;

    public ApiError(String message, Integer errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

}