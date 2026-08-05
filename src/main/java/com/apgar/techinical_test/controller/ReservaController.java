package com.apgar.techinical_test.controller;

import com.apgar.techinical_test.dto.ReservaRequest;
import com.apgar.techinical_test.service.ReservaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody ReservaRequest request) {
        reservaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
