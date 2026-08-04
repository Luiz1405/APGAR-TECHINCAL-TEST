package com.apgar.techinical_test.dto;

import com.apgar.techinical_test.domain.Reserva;
import com.apgar.techinical_test.domain.Sala;

import java.time.OffsetDateTime;

public record ReservaResponse(Sala sala, String responsavel, OffsetDateTime inicio, OffsetDateTime fim) {

    public static ReservaResponse converterDe(Reserva reserva) {
        return new ReservaResponse(reserva.sala(), reserva.responsavel(), reserva.inicio(), reserva.fim());
    }
}
