package com.apgar.techinical_test.validation;

import com.apgar.techinical_test.domain.Reserva;
import com.apgar.techinical_test.domain.Sala;
import com.apgar.techinical_test.dto.ReservaRequest;
import com.apgar.techinical_test.exception.ReservaInvalidaException;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

@Component
public class ReservaValidator {

    private static final LocalTime INICIO_EXPEDIENTE = LocalTime.of(8, 0);
    private static final LocalTime FIM_EXPEDIENTE = LocalTime.of(18, 0);
    private static final int DURACAO_MINIMA_MINUTOS = 30;
    private static final int DURACAO_MAXIMA_MINUTOS = 120;
    private static final int MULTIPLO_MINUTOS_INICIO = 30;

    private final Clock relogio;

    public ReservaValidator(Clock relogio) {
        this.relogio = relogio;
    }

    public Reserva validar(ReservaRequest request, List<Reserva> existentes) {
        validarCamposPreenchidos(request);

        Sala sala = validarSala(request.sala());
        OffsetDateTime inicio = validarFormatoData(request.inicio(), "inicio");
        OffsetDateTime fim = validarFormatoData(request.fim(), "fim");
        Reserva candidata = new Reserva(sala, request.responsavel(), inicio, fim);

        validarFimPosteriorAoInicio(candidata);
        validarDuracaoPermitida(candidata);
        validarNaoIniciaNoPassado(candidata);
        validarJanelaDeHorario(candidata);
        validarMultiploDeTrintaMinutos(candidata);
        validarSemSobreposicao(candidata, existentes);

        return candidata;
    }

    private void validarCamposPreenchidos(ReservaRequest request) {
        boolean algumCampoEmBranco = estaEmBranco(request.sala())
                || estaEmBranco(request.responsavel())
                || estaEmBranco(request.inicio())
                || estaEmBranco(request.fim());
        if (algumCampoEmBranco) {
            throw new ReservaInvalidaException("Os campos sala, responsavel, inicio e fim são obrigatórios.");
        }
    }

    private boolean estaEmBranco(String valor) {
        return valor == null || valor.isBlank();
    }

    private Sala validarSala(String valor) {
        try {
            return Sala.valueOf(valor);
        } catch (IllegalArgumentException excecaoOriginal) {
            throw new ReservaInvalidaException(
                    "Sala '%s' inválida. Salas disponíveis: %s".formatted(valor, Arrays.toString(Sala.values())));
        }
    }

    private OffsetDateTime validarFormatoData(String valor, String nomeCampo) {
        try {
            return OffsetDateTime.parse(valor);
        } catch (DateTimeParseException excecaoOriginal) {
            throw new ReservaInvalidaException(
                    "Campo '%s' deve estar em formato ISO 8601, ex.: 2026-03-08T14:00:00-03:00.".formatted(nomeCampo));
        }
    }

    private void validarFimPosteriorAoInicio(Reserva candidata) {
        if (!candidata.fim().isAfter(candidata.inicio())) {
            throw new ReservaInvalidaException("O horário de fim deve ser posterior ao horário de início.");
        }
    }

    private void validarDuracaoPermitida(Reserva candidata) {
        long duracaoMinutos = Duration.between(candidata.inicio(), candidata.fim()).toMinutes();
        if (duracaoMinutos < DURACAO_MINIMA_MINUTOS || duracaoMinutos > DURACAO_MAXIMA_MINUTOS) {
            throw new ReservaInvalidaException(
                    "A duração da reserva deve ser entre %d e %d minutos.".formatted(DURACAO_MINIMA_MINUTOS,
                            DURACAO_MAXIMA_MINUTOS));
        }
    }

    private void validarNaoIniciaNoPassado(Reserva candidata) {
        if (candidata.inicio().isBefore(OffsetDateTime.now(relogio))) {
            throw new ReservaInvalidaException("A reserva não pode iniciar no passado.");
        }
    }

    private void validarJanelaDeHorario(Reserva candidata) {
        boolean mesmoDia = candidata.inicio().toLocalDate().equals(candidata.fim().toLocalDate());
        boolean dentroDoExpediente = !candidata.inicio().toLocalTime().isBefore(INICIO_EXPEDIENTE)
                && !candidata.fim().toLocalTime().isAfter(FIM_EXPEDIENTE);
        if (!mesmoDia || !dentroDoExpediente) {
            throw new ReservaInvalidaException(
                    "A reserva deve iniciar e terminar no mesmo dia, entre %s e %s.".formatted(INICIO_EXPEDIENTE,
                            FIM_EXPEDIENTE));
        }
    }

    private void validarMultiploDeTrintaMinutos(Reserva candidata) {
        OffsetDateTime inicio = candidata.inicio();
        boolean minutoMultiploDeTrinta = inicio.getMinute() % MULTIPLO_MINUTOS_INICIO == 0;
        boolean semSegundosOuNanos = inicio.getSecond() == 0 && inicio.getNano() == 0;
        if (!minutoMultiploDeTrinta || !semSegundosOuNanos) {
            throw new ReservaInvalidaException(
                    "O horário de início deve ocorrer em múltiplos de %d minutos.".formatted(MULTIPLO_MINUTOS_INICIO));
        }
    }

    private void validarSemSobreposicao(Reserva candidata, List<Reserva> existentes) {
        boolean sobrepoe = existentes.stream()
                .filter(reserva -> reserva.sala() == candidata.sala())
                .anyMatch(reserva -> candidata.inicio().isBefore(reserva.fim())
                        && reserva.inicio().isBefore(candidata.fim()));
        if (sobrepoe) {
            throw new ReservaInvalidaException("A sala já possui uma reserva com sobreposição de horário.");
        }
    }
}
