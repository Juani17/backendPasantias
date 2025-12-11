package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Rol;
import com.Ospuaye.BackendOspuaye.Service.RolService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/roles")
public class RolController extends BaseController<Rol, Long> {

    private final RolService rolService;

    public RolController(RolService service) {
        super(service);
        this.rolService = service;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@Valid @RequestBody Rol rol) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(rolService.crear(rol));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizar(@Valid @RequestBody Rol rol) {
        try {
            return ResponseEntity.ok(rolService.actualizar(rol));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ===============================================
    // BUSQUEDA GLOBAL + PAGINADO (ACTIVOS)
    // ===============================================
    @GetMapping("/buscar")
    public ResponseEntity<Page<Rol>> buscar(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<Rol> result = rolService.buscar(query, page, size);
        return ResponseEntity.ok(result);
    }

    // ===============================================
    // BUSQUEDA GLOBAL + PAGINADO (INACTIVOS)
    // ===============================================
    @GetMapping("/buscar-inactivos")
    public ResponseEntity<Page<Rol>> buscarInactivos(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<Rol> result = rolService.buscarInactivos(query, page, size);
        return ResponseEntity.ok(result);
    }

}
