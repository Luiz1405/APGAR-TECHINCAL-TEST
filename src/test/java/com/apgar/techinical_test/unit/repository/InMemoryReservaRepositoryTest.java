package com.apgar.techinical_test.unit.repository;

import com.apgar.techinical_test.repository.InMemoryReservaRepository;

import com.apgar.techinical_test.domain.Reserva;
import com.apgar.techinical_test.domain.Sala;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryReservaRepositoryTest {

    private final InMemoryReservaRepository repository = new InMemoryReservaRepository();

    // Teste deve garantir que listarTodas retorna lista vazia, nunca null, quando nada foi salvo
    @Test
    void deveRetornarListaVaziaQuandoNaoHaReservasSalvas() {
        assertThat(repository.listarTodas()).isEmpty();
    }

    // Teste deve garantir que uma reserva salva passa a aparecer na listagem
    @Test
    void deveListarReservaAposSalvar() {
        Reserva reserva = criarReserva();

        repository.salvar(reserva);

        assertThat(repository.listarTodas()).containsExactly(reserva);
    }

    // Teste deve garantir que deletarTodas remove todas as reservas salvas
    @Test
    void deveRemoverTodasAsReservasAoDeletarTodas() {
        repository.salvar(criarReserva());

        repository.deletarTodas();

        assertThat(repository.listarTodas()).isEmpty();
    }

    private Reserva criarReserva() {
        return new Reserva(Sala.A101, "Maria",
                OffsetDateTime.parse("2026-08-10T14:00:00-03:00"), OffsetDateTime.parse("2026-08-10T15:00:00-03:00"));
    }
}
