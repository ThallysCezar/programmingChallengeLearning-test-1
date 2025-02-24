package br.com.thallysprojects.pitang_desafio.exceptions.users;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UsersBadRequestException extends RuntimeException {

    private final Integer errorCode;

    public UsersBadRequestException() {
        super("Requisição de forma incorreta para usuário");
        this.errorCode = HttpStatus.BAD_REQUEST.value();
    }

    public UsersBadRequestException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}