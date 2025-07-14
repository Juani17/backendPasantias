package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Base;
import com.Ospuaye.BackendOspuaye.Service.BaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class BaseController<E extends Base, ID extends Serializable> {

    protected BaseService<E, ID> service;

    public BaseController(BaseService<E, ID> service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<E>> listar() throws Exception {
        List<E> entities = service.listar();
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<E>> buscarPorId(@PathVariable ID id) throws Exception {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<E> crear(@RequestBody E entity) throws Exception {
        E entidadCreada = service.crear(entity);
        return ResponseEntity.ok(entidadCreada);
    }

    @PutMapping
    public ResponseEntity<E> actualizar(@RequestBody E entity) throws Exception {
        E entidadActualizada = service.actualizar(entity);
        return ResponseEntity.ok(entidadActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable ID id) throws Exception {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
