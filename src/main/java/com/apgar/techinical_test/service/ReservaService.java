package com.apgar.techinical_test.service;

import com.apgar.techinical_test.domain.Reserva;
import com.apgar.techinical_test.dto.ReservaRequest;
import com.apgar.techinical_test.dto.ReservaResponse;
import com.apgar.techinical_test.repository.Contracts.ReservaRepositoryInterface;
import com.apgar.techinical_test.validation.ReservaValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepositoryInterface reservaRepository;
    private final ReservaValidator reservaValidator;

    public ReservaService(ReservaRepositoryInterface reservaRepository, ReservaValidator reservaValidator) {
        this.reservaRepository = reservaRepository;
        this.reservaValidator = reservaValidator;
    }

    public synchronized void criar(ReservaRequest request) {
        Reserva reserva = reservaValidator.validar(request, reservaRepository.listarTodas());
        reservaRepository.salvar(reserva);
    }

    public List<ReservaResponse> listarTodas() {
        return reservaRepository.listarTodas().stream()
                .map(ReservaResponse::converterDe)
                .toList();
    }
}
