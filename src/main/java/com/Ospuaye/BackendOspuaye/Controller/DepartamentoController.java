package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Departamento;
import com.Ospuaye.BackendOspuaye.Service.DepartamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoController extends BaseController<Departamento, Long> {

    private final DepartamentoService departamentoService;

    public DepartamentoController(DepartamentoService departamentoService) {
        super(departamentoService);
        this.departamentoService = departamentoService;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@Valid @RequestBody Departamento departamento) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(departamentoService.crear(departamento));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/provincia/{provinciaId}")
    public ResponseEntity<?> listarPorProvincia(@PathVariable Long provinciaId) {
        try {
            return ResponseEntity.ok(departamentoService.listarPorProvincia(provinciaId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/activos")
    public ResponseEntity<?> listarActivos() {
        try {
            return ResponseEntity.ok(departamentoService.listarActivos());
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
            return ResponseEntity.ok(departamentoService.buscar(query, page, size));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/buscar-simple")
    public ResponseEntity<?> buscarSimple(@RequestParam String nombre) {
        try {
            return ResponseEntity.ok(departamentoService.buscarSimplePorNombre(nombre));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar departamentos");
        }
    }


}
