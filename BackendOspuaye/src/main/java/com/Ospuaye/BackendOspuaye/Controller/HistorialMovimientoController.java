package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.HistorialMovimiento;
import com.Ospuaye.BackendOspuaye.Service.HistorialMovimientoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/historiales")
public class HistorialMovimientoController extends BaseController<HistorialMovimiento, Long> {

    private final HistorialMovimientoService service;

    public HistorialMovimientoController(HistorialMovimientoService service) {
        super(service);
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody HistorialMovimiento h) {
        try {
            return ResponseEntity.ok(service.crear(h));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
