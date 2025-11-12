package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.BaseNombrable;
import com.Ospuaye.BackendOspuaye.Repository.BaseNombrableRepository;
import jakarta.transaction.Transactional;

import java.io.Serializable;
import java.util.List;

public abstract class BaseNombrableService<E extends BaseNombrable, ID extends Serializable>
        extends BaseService<E, ID> {

    protected final BaseNombrableRepository<E, ID> baseNombrableRepository;

    public BaseNombrableService(BaseNombrableRepository<E, ID> baseNombrableRepository) {
        super(baseNombrableRepository); // ✅ sin cast
        this.baseNombrableRepository = baseNombrableRepository;
    }

    @Transactional
    public List<E> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El parámetro 'nombre' no puede estar vacío");
        }
        return baseNombrableRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Transactional
    public List<E> buscarActivosPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El parámetro 'nombre' no puede estar vacío");
        }
        return baseNombrableRepository.findByActivoTrueAndNombreContainingIgnoreCase(nombre);
    }
}
