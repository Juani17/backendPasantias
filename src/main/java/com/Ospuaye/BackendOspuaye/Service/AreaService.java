package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Area;
import com.Ospuaye.BackendOspuaye.Repository.AreaRepository;
import org.springframework.stereotype.Service;

@Service
public class AreaService extends BaseService<Area, Long> {

    private final AreaRepository areaRepository;

    public AreaService(AreaRepository repository) {
        super(repository);
        this.areaRepository = repository;
    }

    @Override
    public Area crear(Area area) throws Exception {
        if (area.getNombre() == null || area.getNombre().isBlank()) {
            throw new Exception("El nombre del área es obligatorio");
        }
        if (areaRepository.findByNombre(area.getNombre()).isPresent()) {
            throw new Exception("Ya existe un área con ese nombre");
        }
        return areaRepository.save(area);
    }

    @Override
    public Area actualizar(Area area) throws Exception {
        if (area.getId() == null || !areaRepository.existsById(area.getId())) {
            throw new Exception("Área no encontrada");
        }
        if (area.getNombre() != null && !area.getNombre().isBlank()) {
            var existente = areaRepository.findByNombre(area.getNombre());
            if (existente.isPresent() && !existente.get().getId().equals(area.getId())) {
                throw new Exception("El nombre de área ya está en uso");
            }
        }
        return areaRepository.save(area);
    }
}
