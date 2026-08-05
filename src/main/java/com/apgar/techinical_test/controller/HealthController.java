package com.apgar.techinical_test.controller;

import com.apgar.techinical_test.dto.HealthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> verificarSaude() {
        return ResponseEntity.ok(new HealthResponse("Tudo Funcionando aqui"));
    }
}
