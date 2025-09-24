package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Base;
import com.Ospuaye.BackendOspuaye.Repository.BaseRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class BaseService<E extends Base, ID extends Serializable> {

    protected BaseRepository<E, ID> baseRepository;

    public BaseService(BaseRepository<E, ID> baseRepository) {
        this.baseRepository = baseRepository;
    }

    @Transactional(readOnly = true)
    public List<E> listar() throws Exception {
        return baseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<E> buscarPorId(ID id) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        return baseRepository.findById(id);
    }

    @Transactional
    public E crear(E entity) throws Exception {
        if (entity == null) {
            throw new IllegalArgumentException("La entidad no puede ser nula");
        }
        return baseRepository.save(entity);
    }

    @Transactional
    public E actualizar(E entity) throws Exception {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("La entidad o su ID no pueden ser nulos");
        }
        if (!baseRepository.existsById((ID) entity.getId())) {
            throw new IllegalArgumentException("No se encontró la entidad con el ID proporcionado");
        }
        return baseRepository.save(entity);
    }

    @Transactional
    public void eliminar(ID id) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        if (!baseRepository.existsById(id)) {
            throw new IllegalArgumentException("No se encontró la entidad con el ID proporcionado");
        }
        baseRepository.deleteById(id);
    }

    @Transactional
    public E alternarEstado(ID id) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        E entity = baseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la entidad con el ID proporcionado"));

        entity.setActivo(!entity.isActivo()); // invierte el estado actual

        return baseRepository.save(entity);
    }

}
