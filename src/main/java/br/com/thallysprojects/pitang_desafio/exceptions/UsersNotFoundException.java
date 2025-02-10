package br.com.thallysprojects.pitang_desafio.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UsersNotFoundException extends ResponseStatusException {

    public UsersNotFoundException(){
        super(HttpStatus.UNPROCESSABLE_ENTITY, "Usuário não encontrado com esse id");
    }

    public UsersNotFoundException(String message){
        super(HttpStatus.UNPROCESSABLE_ENTITY, message);
    }

}
