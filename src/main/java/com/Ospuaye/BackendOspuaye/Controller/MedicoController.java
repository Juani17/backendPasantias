package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Medico;
import com.Ospuaye.BackendOspuaye.Service.MedicoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController extends BaseController<Medico, Long> {

    private final MedicoService medicoService;

    public MedicoController(MedicoService service) {
        super(service);
        this.medicoService = service;
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Medico medico) {
        try {
            return ResponseEntity.ok(medicoService.crear(medico));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> actualizar(@Valid @RequestBody Medico medico) {
        try {
            return ResponseEntity.ok(medicoService.actualizar(medico));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
