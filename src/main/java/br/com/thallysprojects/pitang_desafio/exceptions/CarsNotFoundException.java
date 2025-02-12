package br.com.thallysprojects.pitang_desafio.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class CarsNotFoundException extends RuntimeException {

    private final Integer errorCode;

    public CarsNotFoundException() {
        super("Carro não encontrado com esse id");
        this.errorCode = HttpStatus.NOT_FOUND.value();
    }

    public CarsNotFoundException(String message, Integer errorCode){
        super(message);
        this.errorCode = errorCode;
    }

}