package com.apgar.techinical_test.feature.controller;

import com.apgar.techinical_test.repository.Contracts.ReservaRepositoryInterface;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.ZoneId;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReservaControllerIntegrationTest {

    // Sempre "amanhã" no fuso da aplicação, para nunca cair no passado,
    // independente de quando os testes rodam.
    private static final String DATA_RESERVA = LocalDate.now(ZoneId.of("America/Sao_Paulo")).plusDays(1).toString();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReservaRepositoryInterface reservaRepository;

    @BeforeEach
    void limparReservas() {
        reservaRepository.deletarTodas();
    }

    // Teste deve garantir que uma reserva válida é criada com 201 sem corpo
    @Test
    void deveCriarReservaERetornar201() throws Exception {
        mockMvc.perform(post("/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reservaJson("A101", "Maria", "09:00:00", "10:00:00")))
                .andExpect(status().isCreated())
                .andExpect(content().string(""));
    }

    // Teste deve garantir que sala inválida retorna 422 com mensagem clara, vinda
    // do TratadorDeExcecoes
    @Test
    void deveRetornar422QuandoSalaForInvalida() throws Exception {
        mockMvc.perform(post("/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reservaJson("Z999", "Maria", "09:00:00", "10:00:00")))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.mensagem").value(Matchers.containsString("inválida")));
    }

    // Teste deve garantir que JSON malformado retorna 400 com a mensagem
    // padronizada, sem stack trace
    @Test
    void deveRetornar400QuandoJsonForMalformado() throws Exception {
        mockMvc.perform(post("/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"sala\":\"A101\","))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("Requisição malformada: verifique o formato do JSON enviado."));
    }

    // Teste deve garantir que uma reserva criada aparece na listagem
    @Test
    void deveListarReservaAposCriar() throws Exception {
        mockMvc.perform(post("/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reservaJson("A101", "Maria", "09:00:00", "10:00:00")));

        mockMvc.perform(get("/reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sala").value("A101"))
                .andExpect(jsonPath("$[0].responsavel").value("Maria"));
    }

    // Teste deve garantir que a listagem vem vazia quando não há reservas
    @Test
    void deveRetornarListaVaziaQuandoNaoHaReservas() throws Exception {
        mockMvc.perform(get("/reservas"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    // Teste deve garantir que deletarTodas esvazia a listagem
    @Test
    void deveEsvaziarListagemAposDeletarTodas() throws Exception {
        mockMvc.perform(post("/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reservaJson("A101", "Maria", "09:00:00", "10:00:00")));

        mockMvc.perform(delete("/reservas")).andExpect(status().isOk());

        mockMvc.perform(get("/reservas"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    private String reservaJson(String sala, String responsavel, String inicioHora, String fimHora) {
        return """
                {"sala":"%s","responsavel":"%s","inicio":"%sT%s-03:00","fim":"%sT%s-03:00"}
                """.formatted(sala, responsavel, DATA_RESERVA, inicioHora, DATA_RESERVA, fimHora);
    }
}
