package com.apgar.techinical_test.service;

import com.apgar.techinical_test.domain.Reserva;
import com.apgar.techinical_test.dto.EstatisticasResponse;
import com.apgar.techinical_test.repository.Contracts.ReservaRepositoryInterface;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Service
public class EstatisticaService {

    private final ReservaRepositoryInterface reservaRepository;
    private final Clock relogio;

    public EstatisticaService(ReservaRepositoryInterface reservaRepository, Clock relogio) {
        this.reservaRepository = reservaRepository;
        this.relogio = relogio;
    }

    public EstatisticasResponse calcular() {
        List<Reserva> reservasDeHoje = filtrarReservasDeHoje(reservaRepository.listarTodas());
        if (reservasDeHoje.isEmpty()) {
            return new EstatisticasResponse(0, 0, 0, 0.0, 0);
        }

        List<Long> duracoesEmMinutos = reservasDeHoje.stream().map(this::duracaoEmMinutos).toList();
        long tempoTotalReservadoMinutos = duracoesEmMinutos.stream().mapToLong(Long::longValue).sum();
        long maiorDuracaoMinutos = duracoesEmMinutos.stream().mapToLong(Long::longValue).max().orElse(0);

        return new EstatisticasResponse(
                reservasDeHoje.size(),
                contarSalasDistintas(reservasDeHoje),
                tempoTotalReservadoMinutos,
                mediaDuracao(tempoTotalReservadoMinutos, reservasDeHoje.size()),
                maiorDuracaoMinutos);
    }

    private List<Reserva> filtrarReservasDeHoje(List<Reserva> todas) {
        LocalDate hoje = LocalDate.now(relogio);
        return todas.stream().filter(reserva -> reserva.inicio().toLocalDate().equals(hoje)).toList();
    }

    private long duracaoEmMinutos(Reserva reserva) {
        return Duration.between(reserva.inicio(), reserva.fim()).toMinutes();
    }

    private int contarSalasDistintas(List<Reserva> reservas) {
        return (int) reservas.stream().map(Reserva::sala).distinct().count();
    }

    private double mediaDuracao(long tempoTotalReservadoMinutos, int countReservas) {
        return (double) tempoTotalReservadoMinutos / countReservas;
    }
}
