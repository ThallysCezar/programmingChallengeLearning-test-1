package br.com.thallysprojects.pitang_desafio.exceptions.users;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UsersGeneralException extends RuntimeException {

    private final Integer errorCode;

    public UsersGeneralException() {
        super("Erro geral da classe Users");
        this.errorCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public UsersGeneralException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}