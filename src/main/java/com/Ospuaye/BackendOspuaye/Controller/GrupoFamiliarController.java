package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.GrupoFamiliar;
import com.Ospuaye.BackendOspuaye.Service.GrupoFamiliarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/grupoFamiliar")
public class GrupoFamiliarController extends BaseController<GrupoFamiliar, Long> {

    private final GrupoFamiliarService service;

    public GrupoFamiliarController(GrupoFamiliarService service) {
        super(service);
        this.service = service;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@Valid @RequestBody GrupoFamiliar gf) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(gf));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizar(@Valid @RequestBody GrupoFamiliar gf) {
        try {
            return ResponseEntity.ok(service.actualizar(gf));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
