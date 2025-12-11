package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Nacionalidad;
import com.Ospuaye.BackendOspuaye.Service.NacionalidadService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nacionalidades")
public class NacionalidadController extends BaseController<Nacionalidad, Long> {

    private final NacionalidadService nacionalidadService;

    public NacionalidadController(NacionalidadService nacionalidadService) {
        super(nacionalidadService);
        this.nacionalidadService = nacionalidadService;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody Nacionalidad entity) {
        try {
            Nacionalidad creado = nacionalidadService.crear(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Nacionalidad entity) {
        try {
            entity.setId(id);
            Nacionalidad actualizado = nacionalidadService.actualizar(entity);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/activas")
    public ResponseEntity<List<Nacionalidad>> listarActivas() {
        try {
            List<Nacionalidad> lista = nacionalidadService.listarActivas();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> listarPorNombre(@PathVariable String nombre) {
        try {
            var nacionalidad = nacionalidadService.ListarPorNombre(nombre);
            return ResponseEntity.ok(nacionalidad);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar la nacionalidad: " + e.getMessage());
        }
    }

    // ===============================================
    // BUSQUEDA GLOBAL + PAGINADO (ACTIVOS)
    // ===============================================
    @GetMapping("/buscar")
    public ResponseEntity<Page<Nacionalidad>> buscar(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<Nacionalidad> result = nacionalidadService.buscar(query, page, size);
        return ResponseEntity.ok(result);
    }

    // ===============================================
    // BUSQUEDA GLOBAL + PAGINADO (INACTIVOS)
    // ===============================================
    @GetMapping("/buscar-inactivos")
    public ResponseEntity<Page<Nacionalidad>> buscarInactivos(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<Nacionalidad> result = nacionalidadService.buscarInactivos(query, page, size);
        return ResponseEntity.ok(result);
    }



}
