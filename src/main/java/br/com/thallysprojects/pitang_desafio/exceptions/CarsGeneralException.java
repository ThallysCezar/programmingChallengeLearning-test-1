package br.com.thallysprojects.pitang_desafio.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CarsGeneralException extends ResponseStatusException {

    public CarsGeneralException(){
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Erro geral da classe Cars");
    }

    public CarsGeneralException(String message){
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

}
