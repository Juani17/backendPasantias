package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.GrupoFamiliar;
import com.Ospuaye.BackendOspuaye.Service.GrupoFamiliarService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/grupoFamiliar")
public class GrupoFamiliarController extends BaseController<GrupoFamiliar, Long> {

    private final GrupoFamiliarService service;

    public GrupoFamiliarController(GrupoFamiliarService service) {
        super(service);
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody GrupoFamiliar gf) {
        try {
            return ResponseEntity.ok(service.crear(gf));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> actualizar(@Valid @RequestBody GrupoFamiliar gf) {
        try {
            return ResponseEntity.ok(service.actualizar(gf));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
