package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Provincia;
import com.Ospuaye.BackendOspuaye.Service.ProvinciaService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/provincias")
public class ProvinciaController extends BaseController<Provincia, Long> {

    private final ProvinciaService provinciaService;

    public ProvinciaController(ProvinciaService provinciaService) {
        super(provinciaService);
        this.provinciaService = provinciaService;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody Provincia entity) {
        try {
            Provincia creado = provinciaService.crear(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<Provincia>> buscar(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(provinciaService.buscar(query, page, size));
    }

    @GetMapping("/buscar-inactivos")
    public ResponseEntity<Page<Provincia>> buscarInactivos(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(provinciaService.buscarInactivos(query, page, size));
    }


    @GetMapping("/buscar-simple")
    public ResponseEntity<?> buscarSimple(@RequestParam String nombre) {
        try {
            return ResponseEntity.ok(provinciaService.buscarPorNombre(nombre));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar provincias");
        }
    }

}
