package br.com.thallysprojects.pitang_desafio.exceptions.cars;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class CarsGeneralException extends RuntimeException {

    private final Integer errorCode;

    public CarsGeneralException() {
        super("Erro geral da classe Cars");
        this.errorCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public CarsGeneralException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}