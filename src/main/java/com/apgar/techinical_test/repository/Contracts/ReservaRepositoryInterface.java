package com.apgar.techinical_test.repository.Contracts;

import com.apgar.techinical_test.domain.Reserva;

import java.util.List;

public interface ReservaRepositoryInterface {

    List<Reserva> listarTodas();

    void salvar(Reserva reserva);

    void deletarTodas();
}
