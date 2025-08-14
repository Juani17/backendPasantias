package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Familiar;
import com.Ospuaye.BackendOspuaye.Repository.FamiliarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FamiliarService extends BaseService<Familiar, Long> {

    private final FamiliarRepository repository;

    public FamiliarService(FamiliarRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    @Transactional
    public Familiar crear(Familiar f) throws Exception {
        if (f == null) throw new Exception("Familiar no puede ser nulo");
        if (f.getNombre() == null || f.getNombre().trim().isEmpty()) throw new Exception("Nombre obligatorio");
        if (f.getTipoParentesco() == null) throw new Exception("Tipo parentesco obligatorio");
        return repository.save(f);
    }

    @Override
    @Transactional
    public Familiar actualizar(Familiar f) throws Exception {
        if (f == null || f.getId() == null) throw new Exception("ID obligatorio para actualizar");
        if (!repository.existsById(f.getId())) throw new Exception("Familiar no encontrado");
        return crear(f);
    }
}
