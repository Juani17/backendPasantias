package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Rol;
import com.Ospuaye.BackendOspuaye.Repository.RolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RolService extends BaseService<Rol, Long> {

    private final RolRepository rolRepository;

    public RolService(RolRepository repository) {
        super(repository);
        this.rolRepository = repository;
    }

    @Override
    @Transactional
    public Rol crear(Rol rol) throws Exception {
        if (rol == null) throw new Exception("Rol no puede ser nulo");
        if (rol.getNombre() == null || rol.getNombre().trim().isEmpty()) throw new Exception("Nombre de rol obligatorio");
        return rolRepository.save(rol);
    }

    @Override
    @Transactional
    public Rol actualizar(Rol rol) throws Exception {
        if (rol == null || rol.getId() == null) throw new Exception("ID obligatorio");
        if (!rolRepository.existsById(rol.getId())) throw new Exception("Rol no encontrado");
        return crear(rol);
    }
}
