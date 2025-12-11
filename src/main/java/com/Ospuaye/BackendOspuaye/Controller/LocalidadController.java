package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Localidad;
import com.Ospuaye.BackendOspuaye.Service.LocalidadService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/localidades")
public class LocalidadController extends BaseController<Localidad, Long> {

    private final LocalidadService localidadService;

    public LocalidadController(LocalidadService localidadService) {
        super(localidadService);
        this.localidadService = localidadService;
    }

    @GetMapping("/activas")
    public ResponseEntity<?> listarActivas() {
        try {
            List<Localidad> lista = localidadService.listarActivas();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/departamento/{departamentoId}")
    public ResponseEntity<?> listarPorDepartamento(@PathVariable Long departamentoId) {
        try {
            List<Localidad> lista = localidadService.listarPorDepartamento(departamentoId);
            return ResponseEntity.ok(lista);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // ===============================================
    // BUSQUEDA GLOBAL + PAGINADO (ACTIVOS)
    // ===============================================
    @GetMapping("/buscar")
    public ResponseEntity<Page<Localidad>> buscar(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<Localidad> result = localidadService.buscar(query, page, size);
        return ResponseEntity.ok(result);
    }

    // ===============================================
    // BUSQUEDA GLOBAL + PAGINADO (INACTIVOS)
    // ===============================================
    @GetMapping("/buscar-inactivos")
    public ResponseEntity<Page<Localidad>> buscarInactivos(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<Localidad> result = localidadService.buscarInactivos(query, page, size);
        return ResponseEntity.ok(result);
    }


    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarLocalidad(
            @PathVariable Long id,
            @RequestBody Localidad localidad
    ) {
        try {
            return ResponseEntity.ok(localidadService.actualizarLocalidad(id, localidad));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buscar-simple")
    public ResponseEntity<?> buscarSimple(@RequestParam String nombre) {
        try {
            return ResponseEntity.ok(localidadService.buscarSimplePorNombre(nombre));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar localidades");
        }
    }
}
