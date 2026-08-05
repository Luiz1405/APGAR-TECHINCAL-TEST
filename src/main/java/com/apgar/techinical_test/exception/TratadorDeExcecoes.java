package com.apgar.techinical_test.exception;

import com.apgar.techinical_test.dto.ErroResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Tratador global de excecoes
 */
@RestControllerAdvice
public class TratadorDeExcecoes {

    @ExceptionHandler(ReservaInvalidaException.class)
    public ResponseEntity<ErroResponse> tratarReservaInvalida(ReservaInvalidaException excecao) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErroResponse(excecao.getMessage()));
    }
}
