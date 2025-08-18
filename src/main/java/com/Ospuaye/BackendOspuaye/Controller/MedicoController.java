package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Medico;
import com.Ospuaye.BackendOspuaye.Service.MedicoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController extends BaseController<Medico, Long> {

    private final MedicoService service;

    public MedicoController(MedicoService service) {
        super(service);
        this.service = service;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@Valid @RequestBody Medico medico) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(medico));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizar(@Valid @RequestBody Medico medico) {
        try {
            return ResponseEntity.ok(service.actualizar(medico));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
