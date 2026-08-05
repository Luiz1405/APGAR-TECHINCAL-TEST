package com.apgar.techinical_test.controller;

import com.apgar.techinical_test.dto.EstatisticasResponse;
import com.apgar.techinical_test.service.EstatisticaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estatisticas")
public class EstatisticaController {

    private final EstatisticaService estatisticaService;

    public EstatisticaController(EstatisticaService estatisticaService) {
        this.estatisticaService = estatisticaService;
    }

    @Operation(summary = "Estatísticas de ocupação do dia corrente")
    @ApiResponse(responseCode = "200", description = "Estatísticas calculadas (zeradas se não houver reservas hoje)")
    @GetMapping
    public ResponseEntity<EstatisticasResponse> consultar() {
        return ResponseEntity.ok(estatisticaService.calcular());
    }
}
