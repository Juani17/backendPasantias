package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Area;
import com.Ospuaye.BackendOspuaye.Repository.AreaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AreaService extends BaseService<Area, Long> {

    private final AreaRepository repository;

    public AreaService(AreaRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    @Transactional
    public Area crear(Area a) throws Exception {
        if (a == null) throw new Exception("Área no puede ser nula");
        if (a.getNombre() == null || a.getNombre().trim().isEmpty()) throw new Exception("Nombre de área obligatorio");
        return repository.save(a);
    }

    @Override
    @Transactional
    public Area actualizar(Area a) throws Exception {
        if (a == null || a.getId() == null) throw new Exception("ID obligatorio");
        if (!repository.existsById(a.getId())) throw new Exception("Área no encontrada");
        return crear(a);
    }
}
