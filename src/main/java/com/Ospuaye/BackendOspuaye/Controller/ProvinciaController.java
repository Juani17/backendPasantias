package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Provincia;
import com.Ospuaye.BackendOspuaye.Service.ProvinciaService;
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
}
