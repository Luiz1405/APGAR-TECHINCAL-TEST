package com.apgar.techinical_test.domain;

import java.time.OffsetDateTime;

public record Reserva(Sala sala, String responsavel, OffsetDateTime inicio, OffsetDateTime fim) {
}
