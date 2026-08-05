package com.apgar.techinical_test.repository;

import com.apgar.techinical_test.domain.Reserva;
import com.apgar.techinical_test.repository.Contracts.ReservaRepositoryInterface;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryReservaRepository implements ReservaRepositoryInterface {

    private final List<Reserva> reservas = new ArrayList<>();

    @Override
    public synchronized List<Reserva> listarTodas() {
        return List.copyOf(reservas);
    }

    @Override
    public synchronized void salvar(Reserva reserva) {
        reservas.add(reserva);
    }
}
