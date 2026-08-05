package com.apgar.techinical_test.unit.service;

import com.apgar.techinical_test.service.EstatisticaService;

import com.apgar.techinical_test.domain.Reserva;
import com.apgar.techinical_test.domain.Sala;
import com.apgar.techinical_test.dto.EstatisticasResponse;
import com.apgar.techinical_test.repository.Contracts.ReservaRepositoryInterface;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class EstatisticaServiceTest {

    private final Clock relogioFixo = Clock.fixed(Instant.parse("2026-08-10T15:00:00Z"),
            ZoneId.of("America/Sao_Paulo"));

    @Mock
    private ReservaRepositoryInterface reservaRepository;

    // Teste deve garantir que as estatísticas vêm zeradas quando não há nenhuma
    // reserva armazenada
    @Test
    void deveRetornarEstatisticasZeradasQuandoNaoHaReservas() {
        given(reservaRepository.listarTodas()).willReturn(List.of());
        EstatisticaService estatisticaService = new EstatisticaService(reservaRepository, relogioFixo);

        EstatisticasResponse resultado = estatisticaService.calcular();

        assertThat(resultado).isEqualTo(new EstatisticasResponse(0, 0, 0, 0.0, 0));
    }

    // Teste deve garantir que reservas de outros dias são ignoradas no cálculo do
    // dia corrente
    @Test
    void deveRetornarEstatisticasZeradasQuandoReservasExistemMasNaoSaoDeHoje() {
        Reserva reservaDeOntem = criarReserva(Sala.A101, "2026-08-09T09:00:00-03:00", "2026-08-09T10:00:00-03:00");
        given(reservaRepository.listarTodas()).willReturn(List.of(reservaDeOntem));
        EstatisticaService estatisticaService = new EstatisticaService(reservaRepository, relogioFixo);

        EstatisticasResponse resultado = estatisticaService.calcular();

        assertThat(resultado).isEqualTo(new EstatisticasResponse(0, 0, 0, 0.0, 0));
    }

    // Teste deve garantir o cálculo correto replicando o exemplo do item 3.3.3 do
    // desafio (8 reservas, 3 salas, 420min, media 52.5, maior 120)
    @Test
    void deveCalcularEstatisticasDoDiaReplicandoOExemploDoDesafio() {
        List<Reserva> reservasDeHoje = List.of(
                criarReserva(Sala.A101, "2026-08-10T09:00:00-03:00", "2026-08-10T11:00:00-03:00"),
                criarReserva(Sala.A101, "2026-08-10T11:00:00-03:00", "2026-08-10T12:00:00-03:00"),
                criarReserva(Sala.A101, "2026-08-10T12:00:00-03:00", "2026-08-10T12:30:00-03:00"),
                criarReserva(Sala.B201, "2026-08-10T09:00:00-03:00", "2026-08-10T10:00:00-03:00"),
                criarReserva(Sala.B201, "2026-08-10T10:00:00-03:00", "2026-08-10T10:30:00-03:00"),
                criarReserva(Sala.C301, "2026-08-10T09:00:00-03:00", "2026-08-10T10:00:00-03:00"),
                criarReserva(Sala.C301, "2026-08-10T10:00:00-03:00", "2026-08-10T10:30:00-03:00"),
                criarReserva(Sala.C301, "2026-08-10T10:30:00-03:00", "2026-08-10T11:00:00-03:00"));
        given(reservaRepository.listarTodas()).willReturn(reservasDeHoje);
        EstatisticaService estatisticaService = new EstatisticaService(reservaRepository, relogioFixo);

        EstatisticasResponse resultado = estatisticaService.calcular();

        assertThat(resultado).isEqualTo(new EstatisticasResponse(8, 3, 420, 52.5, 120));
    }

    private Reserva criarReserva(Sala sala, String inicio, String fim) {
        return new Reserva(sala, "Maria", OffsetDateTime.parse(inicio), OffsetDateTime.parse(fim));
    }
}
