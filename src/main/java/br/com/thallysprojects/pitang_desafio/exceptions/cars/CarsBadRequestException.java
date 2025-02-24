package br.com.thallysprojects.pitang_desafio.exceptions.cars;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CarsBadRequestException extends RuntimeException {

    private final Integer errorCode;

    public CarsBadRequestException() {
        super("Requisição de forma incorreta para o carro");
        this.errorCode = HttpStatus.BAD_REQUEST.value();
    }

    public CarsBadRequestException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}