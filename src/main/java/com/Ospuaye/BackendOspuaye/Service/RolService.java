package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Area;
import com.Ospuaye.BackendOspuaye.Entity.Rol;
import com.Ospuaye.BackendOspuaye.Repository.AreaRepository;
import com.Ospuaye.BackendOspuaye.Repository.RolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RolService extends BaseNombrableService<Rol, Long> {

    private final RolRepository rolRepository;
    private final AreaRepository areaRepository;

    public RolService(RolRepository rolRepository, AreaRepository areaRepository) {
        super(rolRepository);
        this.rolRepository = rolRepository;
        this.areaRepository = areaRepository;
    }

    @Override
    @Transactional
    public Rol crear(Rol rol) throws Exception {
        if (rol.getNombre() == null || rol.getNombre().isBlank())
            throw new Exception("El nombre del rol es obligatorio");

        Optional<Rol> existente = rolRepository.findByNombre(rol.getNombre());
        if (existente.isPresent())
            throw new Exception("Ya existe un rol con ese nombre");

        validarArea(rol.getArea());
        return rolRepository.save(rol);
    }

    @Override
    @Transactional
    public Rol actualizar(Rol rol) throws Exception {
        if (rol.getId() == null || !rolRepository.existsById(rol.getId()))
            throw new Exception("Rol no encontrado");

        if (rol.getNombre() != null && !rol.getNombre().isBlank()) {
            Optional<Rol> porNombre = rolRepository.findByNombre(rol.getNombre());
            if (porNombre.isPresent() && !porNombre.get().getId().equals(rol.getId()))
                throw new Exception("El nombre del rol ya está en uso");
        }

        if (rol.getArea() != null) validarArea(rol.getArea());

        Rol existente = rolRepository.findById(rol.getId()).get();
        if (rol.getNombre() != null) existente.setNombre(rol.getNombre());
        if (rol.getArea() != null) existente.setArea(rol.getArea());

        return rolRepository.save(existente);
    }

    private void validarArea(Area area) throws Exception {
        if (area == null || area.getId() == null || !areaRepository.existsById(area.getId()))
            throw new Exception("El área asociada no existe");
    }
}
