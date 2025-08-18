package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.HistorialMovimiento;
import com.Ospuaye.BackendOspuaye.Service.HistorialMovimientoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/historiales")
public class HistorialMovimientoController extends BaseController<HistorialMovimiento, Long> {

    private final HistorialMovimientoService service;

    public HistorialMovimientoController(HistorialMovimientoService service) {
        super(service);
        this.service = service;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@Valid @RequestBody HistorialMovimiento h) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(h));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizar(@Valid @RequestBody HistorialMovimiento h) {
        try {
            return ResponseEntity.ok(service.actualizar(h));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
