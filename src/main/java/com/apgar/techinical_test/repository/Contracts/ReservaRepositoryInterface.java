package com.apgar.techinical_test.repository.Contracts;

import com.apgar.techinical_test.domain.Reserva;

import java.util.List;

public interface ReservaRepositoryInterface {

    /**
     * @return todas as reservas armazenadas; lista vazia quando não houver nenhuma.
     */
    List<Reserva> listarTodas();

    void salvar(Reserva reserva);
}
