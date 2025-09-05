package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Pais;
import com.Ospuaye.BackendOspuaye.Service.PaisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paises")
public class PaisController extends BaseController<Pais, Long> {

    private final PaisService paisService;

    public PaisController(PaisService paisService) {
        super(paisService);
        this.paisService = paisService;
    }

    @GetMapping("/activos")
    public ResponseEntity<?> listarActivos() {
        try {
            List<Pais> activos = paisService.listarActivos();
            return ResponseEntity.ok(activos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al listar los países activos: " + e.getMessage());
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearPais(@RequestBody Pais pais) {
        try {
            Pais creado = paisService.crear(pais);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarPais(@PathVariable Long id, @RequestBody Pais pais) {
        try {
            pais.setId(id);
            Pais actualizado = paisService.actualizar(pais);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
