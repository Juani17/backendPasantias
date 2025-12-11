package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.GrupoFamiliar;
import com.Ospuaye.BackendOspuaye.Service.GrupoFamiliarService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    // ===============================================
    // BUSQUEDA GLOBAL + PAGINADO (ACTIVOS)
    // ===============================================
    @GetMapping("/buscar")
    public ResponseEntity<Page<GrupoFamiliar>> buscar(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<GrupoFamiliar> result = service.buscar(query, page, size);
        return ResponseEntity.ok(result);
    }

    // ===============================================
    // BUSQUEDA GLOBAL + PAGINADO (INACTIVOS)
    // ===============================================
    @GetMapping("/buscar-inactivos")
    public ResponseEntity<Page<GrupoFamiliar>> buscarInactivos(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<GrupoFamiliar> result = service.buscarInactivos(query, page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}/export")
    public ResponseEntity<Resource> exportarGrupo(@PathVariable Long id) {

        ByteArrayResource r = service.exportarTXT(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"grupo_familiar_" + id + ".txt\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(r);
    }


}
