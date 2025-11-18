package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Domicilio;
import com.Ospuaye.BackendOspuaye.Service.DomicilioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/domicilios")
public class DomicilioController extends BaseController<Domicilio, Long> {

    private final DomicilioService domicilioService;

    public DomicilioController(DomicilioService domicilioService) {
        super(domicilioService);
        this.domicilioService = domicilioService;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@Valid @RequestBody Domicilio domicilio) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(domicilioService.crear(domicilio));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/localidad/{localidadId}")
    public ResponseEntity<?> listarPorLocalidad(@PathVariable Long localidadId) {
        try {
            List<Domicilio> lista = domicilioService.listarPorLocalidad(localidadId);
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/activos")
    public ResponseEntity<?> listarActivos() {
        try {
            List<Domicilio> lista = domicilioService.listarActivos();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscar(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        try {
            return ResponseEntity.ok(domicilioService.buscar(query, page, size));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
