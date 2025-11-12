package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.BaseNombrable;
import com.Ospuaye.BackendOspuaye.Service.BaseNombrableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;

public abstract class BaseNombrableController<E extends BaseNombrable, ID extends Serializable>
        extends BaseController<E, ID> {

    protected final BaseNombrableService<E, ID> baseNombrableService;

    public BaseNombrableController(BaseNombrableService<E, ID> baseNombrableService) {
        super(baseNombrableService);
        this.baseNombrableService = baseNombrableService;
    }

    @GetMapping("/buscar/nombre")
    public ResponseEntity<?> buscarPorNombre(@RequestParam String nombre) {
        try {
            return ResponseEntity.ok(baseNombrableService.buscarPorNombre(nombre));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buscar/activos")
    public ResponseEntity<?> buscarActivosPorNombre(@RequestParam String nombre) {
        try {
            return ResponseEntity.ok(baseNombrableService.buscarActivosPorNombre(nombre));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
