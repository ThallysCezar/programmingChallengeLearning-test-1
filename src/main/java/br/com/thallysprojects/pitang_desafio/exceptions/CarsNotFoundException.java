package br.com.thallysprojects.pitang_desafio.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CarsNotFoundException extends ResponseStatusException {

    public CarsNotFoundException(){
        super(HttpStatus.UNPROCESSABLE_ENTITY, "Carro não encontrado com esse id");
    }

    public CarsNotFoundException(String message){
        super(HttpStatus.UNPROCESSABLE_ENTITY, message);
    }

}
