package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Familiar;
import com.Ospuaye.BackendOspuaye.Service.FamiliarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/familiares")
public class FamiliarController extends BaseController<Familiar, Long> {

    private final FamiliarService familiarService;

    public FamiliarController(FamiliarService service) {
        super(service);
        this.familiarService = service;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@Valid @RequestBody Familiar familiar) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(familiarService.crear(familiar));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizar(@Valid @RequestBody Familiar familiar) {
        try {
            return ResponseEntity.ok(familiarService.actualizar(familiar));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/beneficiario/{beneficiarioId}")
    public ResponseEntity<?> listarPorBeneficiario(@PathVariable Long beneficiarioId) {
        try {
            List<Familiar> familiares = familiarService.listarPorBeneficiario(beneficiarioId);
            return ResponseEntity.ok(familiares);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
