package com.apgar.techinical_test.unit.exception;

import com.apgar.techinical_test.exception.TratadorDeExcecoes;
import com.apgar.techinical_test.exception.ReservaInvalidaException;

import com.apgar.techinical_test.dto.ErroResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import static org.assertj.core.api.Assertions.assertThat;

class TratadorDeExcecoesTest {

    private final TratadorDeExcecoes tratadorDeExcecoes = new TratadorDeExcecoes();

    // Teste deve garantir que ReservaInvalidaException é convertida em 422 com a mensagem original da exceção
    @Test
    void deveConverterReservaInvalidaExceptionEm422() {
        ResponseEntity<ErroResponse> resposta = tratadorDeExcecoes.tratarReservaInvalida(
                new ReservaInvalidaException("sala inválida"));

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_CONTENT);
        assertThat(resposta.getBody()).isEqualTo(new ErroResponse("sala inválida"));
    }

    // Teste deve garantir que JSON malformado é convertido em 400 com mensagem padronizada, sem vazar a mensagem original da exceção
    @Test
    void deveConverterHttpMessageNotReadableExceptionEm400() {
        String mensagemOriginal = "mensagem tecnica qualquer";
        ResponseEntity<ErroResponse> resposta = tratadorDeExcecoes.tratarRequisicaoMalformada(
                new HttpMessageNotReadableException(mensagemOriginal, (org.springframework.http.HttpInputMessage) null));

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resposta.getBody().mensagem()).doesNotContain(mensagemOriginal);
    }
}
