package com.apgar.techinical_test.exception;

import com.apgar.techinical_test.dto.ErroResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Tratador global de excecoes.
 *
 */
@RestControllerAdvice
public class TratadorDeExcecoes {

    @ExceptionHandler(ReservaInvalidaException.class)
    public ResponseEntity<ErroResponse> tratarReservaInvalida(ReservaInvalidaException excecao) {
        return construirResposta(HttpStatus.UNPROCESSABLE_CONTENT, excecao.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> tratarRequisicaoMalformada(HttpMessageNotReadableException excecao) {
        return construirResposta(HttpStatus.BAD_REQUEST, "Requisição malformada: verifique o formato do JSON enviado.");
    }

    private ResponseEntity<ErroResponse> construirResposta(HttpStatus status, String mensagem) {
        return ResponseEntity.status(status).body(new ErroResponse(mensagem));
    }
}
