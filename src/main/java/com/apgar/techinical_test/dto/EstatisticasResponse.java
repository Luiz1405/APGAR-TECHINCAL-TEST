package com.apgar.techinical_test.dto;

public record EstatisticasResponse(
        int countReservas,
        int salasUtilizadas,
        long tempoTotalReservadoMinutos,
        double mediaDuracaoMinutos,
        long maiorDuracaoMinutos) {
}
