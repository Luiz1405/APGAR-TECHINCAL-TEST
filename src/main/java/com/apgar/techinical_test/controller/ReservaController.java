package com.apgar.techinical_test.controller;

import com.apgar.techinical_test.dto.ErroResponse;
import com.apgar.techinical_test.dto.ReservaRequest;
import com.apgar.techinical_test.dto.ReservaResponse;
import com.apgar.techinical_test.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @Operation(summary = "Cria uma reserva de sala")
    @ApiResponse(responseCode = "201", description = "Reserva criada com sucesso")
    @ApiResponse(responseCode = "422", description = "Reserva inválida por regra de negócio",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    @ApiResponse(responseCode = "400", description = "Requisição malformada",
            content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody ReservaRequest request) {
        reservaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Lista todas as reservas armazenadas")
    @ApiResponse(responseCode = "200", description = "Lista de reservas (vazia se não houver nenhuma)")
    @GetMapping
    public ResponseEntity<List<ReservaResponse>> listar() {
        return ResponseEntity.ok(reservaService.listarTodas());
    }

    @Operation(summary = "Remove todas as reservas armazenadas")
    @ApiResponse(responseCode = "200", description = "Reservas removidas")
    @DeleteMapping
    public ResponseEntity<Void> deletarTodas() {
        reservaService.deletarTodas();
        return ResponseEntity.ok().build();
    }
}
