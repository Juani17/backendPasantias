package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.GrupoFamiliar;
import com.Ospuaye.BackendOspuaye.Repository.GrupoFamiliarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GrupoFamiliarService extends BaseService<GrupoFamiliar, Long> {

    private final GrupoFamiliarRepository repository;

    public GrupoFamiliarService(GrupoFamiliarRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    @Transactional
    public GrupoFamiliar crear(GrupoFamiliar gf) throws Exception {
        if (gf == null) throw new Exception("Grupo familiar no puede ser nulo");
        if (gf.getNombreGrupo() == null || gf.getNombreGrupo().trim().isEmpty())
            throw new Exception("Nombre de grupo es obligatorio");
        if (gf.getTitular() == null || gf.getTitular().getId() == null)
            throw new Exception("Titular del grupo es obligatorio");
        return repository.save(gf);
    }

    @Override
    @Transactional
    public GrupoFamiliar actualizar(GrupoFamiliar gf) throws Exception {
        if (gf == null || gf.getId() == null) throw new Exception("ID es obligatorio para actualizar");
        if (!repository.existsById(gf.getId())) throw new Exception("Grupo familiar no encontrado");
        return crear(gf);
    }
}
