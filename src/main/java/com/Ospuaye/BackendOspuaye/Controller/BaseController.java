package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Base;
import com.Ospuaye.BackendOspuaye.Service.BaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

public abstract class BaseController<E extends Base, ID extends Serializable> {

    protected BaseService<E, ID> baseService;

    public BaseController(BaseService<E, ID> baseService) {
        this.baseService = baseService;
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            List<E> lista = baseService.listar();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable ID id) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().body("El ID no puede ser nulo");
            }
            return ResponseEntity.ok(baseService.buscarPorId(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody E entity) {
        try {
            if (entity == null) {
                return ResponseEntity.badRequest().body("La entidad no puede ser nula");
            }
            return ResponseEntity.ok(baseService.crear(entity));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable ID id, @RequestBody E entity) {
        try {
            if (entity == null) {
                return ResponseEntity.badRequest().body("La entidad no puede ser nula");
            }
            entity.setId((Long) id); // Aseguramos que use el ID correcto
            return ResponseEntity.ok(baseService.actualizar(entity));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable ID id) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().body("El ID no puede ser nulo");
            }
            baseService.eliminar(id);
            return ResponseEntity.ok("Registro eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> alternarEstado(@PathVariable ID id) {
        try {
            return ResponseEntity.ok(baseService.alternarEstado(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
