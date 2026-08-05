package com.apgar.techinical_test.service;

import com.apgar.techinical_test.domain.Reserva;
import com.apgar.techinical_test.domain.Sala;
import com.apgar.techinical_test.dto.ReservaRequest;
import com.apgar.techinical_test.exception.ReservaInvalidaException;
import com.apgar.techinical_test.repository.Contracts.ReservaRepositoryInterface;
import com.apgar.techinical_test.validation.ReservaValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepositoryInterface reservaRepository;

    @Mock
    private ReservaValidator reservaValidator;

    // Teste deve garantir que a reserva é validada com as reservas existentes e salva quando o validador aprova
    @Test
    void deveValidarComAsReservasExistentesESalvarQuandoValida() {
        ReservaRequest request = new ReservaRequest("A101", "Maria", "2026-08-10T14:00:00-03:00",
                "2026-08-10T15:00:00-03:00");
        List<Reserva> existentes = List.of();
        Reserva reservaValidada = new Reserva(Sala.A101, "Maria",
                OffsetDateTime.parse("2026-08-10T14:00:00-03:00"), OffsetDateTime.parse("2026-08-10T15:00:00-03:00"));
        given(reservaRepository.listarTodas()).willReturn(existentes);
        given(reservaValidator.validar(eq(request), eq(existentes))).willReturn(reservaValidada);
        ReservaService reservaService = new ReservaService(reservaRepository, reservaValidator);

        reservaService.criar(request);

        verify(reservaRepository).salvar(reservaValidada);
    }

    // Teste deve garantir que uma reserva não será salva quando o validador retornar erro
    @Test
    void naoDeveSalvarQuandoOValidadorRejeitarAReserva() {
        ReservaRequest request = new ReservaRequest("A101", "Maria", "2026-08-10T14:00:00-03:00",
                "2026-08-10T14:10:00-03:00");
        given(reservaRepository.listarTodas()).willReturn(List.of());
        given(reservaValidator.validar(any(), any())).willThrow(new ReservaInvalidaException("duração inválida"));
        ReservaService reservaService = new ReservaService(reservaRepository, reservaValidator);

        assertThatThrownBy(() -> reservaService.criar(request)).isInstanceOf(ReservaInvalidaException.class);
        verify(reservaRepository, never()).salvar(any());
    }
}
