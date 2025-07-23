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
    public ResponseEntity<E> obtenerPorId(@PathVariable ID id) throws Exception {
        Optional<E> entity = service.buscarPorId(id);
        return entity.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<E> crear(@RequestBody E entity) throws Exception {
        E nuevo = service.crear(entity);
        return ResponseEntity.ok(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<E> actualizar(@PathVariable ID id, @RequestBody E entity) throws Exception {
        entity.setId((Long) id); // ⚠️ o haz un cast apropiado si no es Long
        E actualizado = service.actualizar(entity);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable ID id) throws Exception {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
