package com.apgar.techinical_test.exception;

/**
 * Lancada quando uma reserva viola alguma regra de negócio.
 * 
 */
public class ReservaInvalidaException extends RuntimeException {

    public ReservaInvalidaException(String mensagem) {
        super(mensagem);
    }
}
