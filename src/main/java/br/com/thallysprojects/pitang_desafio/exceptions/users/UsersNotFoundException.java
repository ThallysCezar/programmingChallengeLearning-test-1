package br.com.thallysprojects.pitang_desafio.exceptions.users;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UsersNotFoundException extends RuntimeException {

    private final Integer errorCode;

    public UsersNotFoundException() {
        super("Usuário não encontrado com esse id");
        this.errorCode = HttpStatus.NO_CONTENT.value();
    }

    public UsersNotFoundException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}