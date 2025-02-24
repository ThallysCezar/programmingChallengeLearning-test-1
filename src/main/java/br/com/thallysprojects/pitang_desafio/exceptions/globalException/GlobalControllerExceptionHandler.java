package br.com.thallysprojects.pitang_desafio.exceptions.globalException;

import br.com.thallysprojects.pitang_desafio.exceptions.cars.CarsBadRequestException;
import br.com.thallysprojects.pitang_desafio.exceptions.cars.CarsGeneralException;
import br.com.thallysprojects.pitang_desafio.exceptions.cars.CarsNotFoundException;
import br.com.thallysprojects.pitang_desafio.exceptions.users.UsersBadRequestException;
import br.com.thallysprojects.pitang_desafio.exceptions.users.UsersGeneralException;
import br.com.thallysprojects.pitang_desafio.exceptions.users.UsersNotFoundException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler({ConversionFailedException.class, UsersBadRequestException.class, CarsBadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleConversion(RuntimeException ex) {
        ApiError error = new ApiError(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({CarsNotFoundException.class, UsersNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiError> handleEntityNotFound(RuntimeException ex) {
        ApiError error = new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({CarsGeneralException.class, UsersGeneralException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiError> handleEntityGeneral(RuntimeException ex) {
        ApiError error = new ApiError(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}