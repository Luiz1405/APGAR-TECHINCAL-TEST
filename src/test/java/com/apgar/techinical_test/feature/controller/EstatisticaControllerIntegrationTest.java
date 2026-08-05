package com.apgar.techinical_test.feature.controller;

import com.apgar.techinical_test.repository.Contracts.ReservaRepositoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EstatisticaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReservaRepositoryInterface reservaRepository;

    @BeforeEach
    void limparReservas() {
        reservaRepository.deletarTodas();
    }

    // Teste deve garantir que as estatísticas vêm zeradas via HTTP quando não há
    // reservas
    @Test
    void deveRetornarEstatisticasZeradasQuandoNaoHaReservas() throws Exception {
        mockMvc.perform(get("/estatisticas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countReservas").value(0))
                .andExpect(jsonPath("$.salasUtilizadas").value(0))
                .andExpect(jsonPath("$.tempoTotalReservadoMinutos").value(0))
                .andExpect(jsonPath("$.mediaDuracaoMinutos").value(0.0))
                .andExpect(jsonPath("$.maiorDuracaoMinutos").value(0));
    }
}
