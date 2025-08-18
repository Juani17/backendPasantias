package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Area;
import com.Ospuaye.BackendOspuaye.Entity.Rol;
import com.Ospuaye.BackendOspuaye.Repository.AreaRepository;
import com.Ospuaye.BackendOspuaye.Repository.RolRepository;
import org.springframework.stereotype.Service;

@Service
public class RolService extends BaseService<Rol, Long> {

    private final RolRepository rolRepository;
    private final AreaRepository areaRepository;

    public RolService(RolRepository rolRepository, AreaRepository areaRepository) {
        super(rolRepository);
        this.rolRepository = rolRepository;
        this.areaRepository = areaRepository;
    }

    @Override
    public Rol crear(Rol rol) throws Exception {
        if (rol.getNombre() == null || rol.getNombre().isBlank()) {
            throw new Exception("El nombre del rol es obligatorio");
        }
        if (rolRepository.findByNombre(rol.getNombre()).isPresent()) {
            throw new Exception("El rol ya existe");
        }
        validarArea(rol.getArea());
        return rolRepository.save(rol);
    }

    @Override
    public Rol actualizar(Rol rol) throws Exception {
        if (rol.getId() == null || !rolRepository.existsById(rol.getId())) {
            throw new Exception("Rol no encontrado");
        }
        if (rol.getNombre() != null && !rol.getNombre().isBlank()) {
            var existente = rolRepository.findByNombre(rol.getNombre());
            if (existente.isPresent() && !existente.get().getId().equals(rol.getId())) {
                throw new Exception("El nombre de rol ya está en uso");
            }
        }
        if (rol.getArea() != null) validarArea(rol.getArea());
        return rolRepository.save(rol);
    }

    private void validarArea(Area area) throws Exception {
        if (area == null || area.getId() == null || !areaRepository.existsById(area.getId())) {
            throw new Exception("El área asociada no existe");
        }
    }
}
