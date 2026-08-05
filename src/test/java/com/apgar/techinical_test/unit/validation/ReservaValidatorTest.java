package com.apgar.techinical_test.unit.validation;

import com.apgar.techinical_test.validation.ReservaValidator;

import com.apgar.techinical_test.domain.Reserva;
import com.apgar.techinical_test.domain.Sala;
import com.apgar.techinical_test.dto.ReservaRequest;
import com.apgar.techinical_test.exception.ReservaInvalidaException;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservaValidatorTest {

    // "Agora" fixo em 2026-08-04T09:00:00-03:00, para os testes não dependerem do horário real da máquina.
    private final Clock relogioFixo = Clock.fixed(Instant.parse("2026-08-04T12:00:00Z"), ZoneOffset.UTC);
    private final ReservaValidator validator = new ReservaValidator(relogioFixo);

    // Teste deve garantir que uma reserva é aceita quando atende a todas as regras de negócio
    @Test
    void deveAceitarReservaQuandoTodasAsRegrasForemAtendidas() {
        ReservaRequest request = criarRequest("A101", "Maria", "2026-08-10T14:00:00-03:00", "2026-08-10T15:00:00-03:00");

        Reserva reserva = validator.validar(request, List.of());

        assertThat(reserva.sala()).isEqualTo(Sala.A101);
    }

    // Teste deve garantir que a reserva é rejeitada quando algum campo obrigatório está em branco
    @Test
    void deveRejeitarQuandoAlgumCampoEstaEmBranco() {
        ReservaRequest request = criarRequest("A101", "", "2026-08-10T14:00:00-03:00", "2026-08-10T15:00:00-03:00");

        assertThatThrownBy(() -> validator.validar(request, List.of()))
                .isInstanceOf(ReservaInvalidaException.class)
                .hasMessageContaining("obrigatórios");
    }

    // Teste deve garantir que a reserva é rejeitada quando a sala informada não existe
    @Test
    void deveRejeitarSalaInexistente() {
        ReservaRequest request = criarRequest("Z999", "Maria", "2026-08-10T14:00:00-03:00", "2026-08-10T15:00:00-03:00");

        assertThatThrownBy(() -> validator.validar(request, List.of()))
                .isInstanceOf(ReservaInvalidaException.class)
                .hasMessageContaining("inválida");
    }

    // Teste deve garantir que a reserva é rejeitada quando inicio ou fim não estão em formato ISO 8601
    @Test
    void deveRejeitarFormatoDeDataInvalido() {
        ReservaRequest request = criarRequest("A101", "Maria", "não-é-uma-data", "2026-08-10T15:00:00-03:00");

        assertThatThrownBy(() -> validator.validar(request, List.of()))
                .isInstanceOf(ReservaInvalidaException.class)
                .hasMessageContaining("ISO 8601");
    }

    // Teste deve garantir que a reserva é rejeitada quando o fim não é posterior ao início
    @Test
    void deveRejeitarQuandoFimNaoEPosteriorAoInicio() {
        ReservaRequest request = criarRequest("A101", "Maria", "2026-08-10T15:00:00-03:00", "2026-08-10T14:00:00-03:00");

        assertThatThrownBy(() -> validator.validar(request, List.of()))
                .isInstanceOf(ReservaInvalidaException.class)
                .hasMessageContaining("posterior");
    }

    // Teste deve garantir que a reserva é rejeitada quando a duração é menor que 30 minutos
    @Test
    void deveRejeitarDuracaoMenorQueTrintaMinutos() {
        ReservaRequest request = criarRequest("A101", "Maria", "2026-08-10T14:00:00-03:00", "2026-08-10T14:15:00-03:00");

        assertThatThrownBy(() -> validator.validar(request, List.of()))
                .isInstanceOf(ReservaInvalidaException.class)
                .hasMessageContaining("duração");
    }

    // Teste deve garantir que a reserva é rejeitada quando a duração é maior que 2 horas
    @Test
    void deveRejeitarDuracaoMaiorQueDuasHoras() {
        ReservaRequest request = criarRequest("A101", "Maria", "2026-08-10T14:00:00-03:00", "2026-08-10T16:30:00-03:00");

        assertThatThrownBy(() -> validator.validar(request, List.of()))
                .isInstanceOf(ReservaInvalidaException.class)
                .hasMessageContaining("duração");
    }

    // Teste deve garantir que a reserva é rejeitada quando o início está no passado
    @Test
    void deveRejeitarReservaComInicioNoPassado() {
        // "agora" fixo é 2026-08-04T09:00:00-03:00
        ReservaRequest request = criarRequest("A101", "Maria", "2026-08-01T14:00:00-03:00", "2026-08-01T15:00:00-03:00");

        assertThatThrownBy(() -> validator.validar(request, List.of()))
                .isInstanceOf(ReservaInvalidaException.class)
                .hasMessageContaining("passado");
    }

    // Teste deve garantir que a reserva é rejeitada quando termina fora da janela de horário permitida (08:00-18:00)
    @Test
    void deveRejeitarReservaForaDaJanelaDeHorarioPermitida() {
        ReservaRequest request = criarRequest("A101", "Maria", "2026-08-10T17:30:00-03:00", "2026-08-10T19:00:00-03:00");

        assertThatThrownBy(() -> validator.validar(request, List.of()))
                .isInstanceOf(ReservaInvalidaException.class)
                .hasMessageContaining("08:00");
    }

    // Teste deve garantir que a reserva é aceita quando termina exatamente às 18:00
    @Test
    void deveAceitarReservaQueTerminaExatamenteNoLimiteDasDezoitoHoras() {
        ReservaRequest request = criarRequest("A101", "Maria", "2026-08-10T17:00:00-03:00", "2026-08-10T18:00:00-03:00");

        Reserva reserva = validator.validar(request, List.of());

        assertThat(reserva.fim().toLocalTime()).isEqualTo(java.time.LocalTime.of(18, 0));
    }

    // Teste deve garantir que a reserva é rejeitada quando o início não é múltiplo de 30 minutos
    @Test
    void deveRejeitarInicioQueNaoEMultiploDeTrintaMinutos() {
        ReservaRequest request = criarRequest("A101", "Maria", "2026-08-10T14:15:00-03:00", "2026-08-10T15:15:00-03:00");

        assertThatThrownBy(() -> validator.validar(request, List.of()))
                .isInstanceOf(ReservaInvalidaException.class)
                .hasMessageContaining("múltiplos de 30");
    }

    // Teste deve garantir que a reserva é rejeitada quando há sobreposição de horário na mesma sala
    @Test
    void deveRejeitarQuandoHaSobreposicaoDeHorarioNaMesmaSala() {
        Reserva existente = new Reserva(Sala.A101, "João",
                java.time.OffsetDateTime.parse("2026-08-10T14:00:00-03:00"),
                java.time.OffsetDateTime.parse("2026-08-10T15:00:00-03:00"));
        ReservaRequest request = criarRequest("A101", "Maria", "2026-08-10T14:30:00-03:00", "2026-08-10T15:30:00-03:00");

        assertThatThrownBy(() -> validator.validar(request, List.of(existente)))
                .isInstanceOf(ReservaInvalidaException.class)
                .hasMessageContaining("sobreposição");
    }

    // Teste deve garantir que reservas contíguas na mesma sala são aceitas (sem sobreposição)
    @Test
    void deveAceitarReservasContiguasNaMesmaSala() {
        Reserva existente = new Reserva(Sala.A101, "João",
                java.time.OffsetDateTime.parse("2026-08-10T14:00:00-03:00"),
                java.time.OffsetDateTime.parse("2026-08-10T15:00:00-03:00"));
        ReservaRequest request = criarRequest("A101", "Maria", "2026-08-10T15:00:00-03:00", "2026-08-10T16:00:00-03:00");

        Reserva reserva = validator.validar(request, List.of(existente));

        assertThat(reserva.inicio().toLocalTime()).isEqualTo(java.time.LocalTime.of(15, 0));
    }

    private ReservaRequest criarRequest(String sala, String responsavel, String inicio, String fim) {
        return new ReservaRequest(sala, responsavel, inicio, fim);
    }
}
