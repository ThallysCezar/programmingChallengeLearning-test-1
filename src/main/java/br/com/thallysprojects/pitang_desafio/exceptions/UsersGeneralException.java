package br.com.thallysprojects.pitang_desafio.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UsersGeneralException extends ResponseStatusException {

    public UsersGeneralException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Erro geral da classe Users");
    }

    public UsersGeneralException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

}