package com.apgar.techinical_test.controller;

import com.apgar.techinical_test.dto.HealthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @Operation(summary = "Verifica se a aplicação está no ar")
    @ApiResponse(responseCode = "200", description = "Aplicação respondendo normalmente")
    @GetMapping("/health")
    public ResponseEntity<HealthResponse> verificarSaude() {
        return ResponseEntity.ok(new HealthResponse("Tudo Funcionando aqui"));
    }
}
