package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Area;
import com.Ospuaye.BackendOspuaye.Repository.AreaRepository;
import org.springframework.stereotype.Service;

@Service
public class AreaService extends BaseNombrableService<Area, Long> {

    private final AreaRepository areaRepository;

    public AreaService(AreaRepository areaRepository) {
        super(areaRepository);
        this.areaRepository = areaRepository;
    }

    @Override
    public Area crear(Area entity) throws Exception {
        if (entity.getNombre() == null || entity.getNombre().isBlank()) {
            throw new Exception("El nombre del área es obligatorio");
        }
        if (areaRepository.existsByNombreIgnoreCase(entity.getNombre())) {
            throw new Exception("Ya existe un área con ese nombre");
        }
        return super.crear(entity);
    }
}
