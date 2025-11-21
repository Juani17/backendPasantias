package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Area;
import com.Ospuaye.BackendOspuaye.Repository.AreaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AreaService extends BaseService<Area, Long> {

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

    @Transactional(readOnly = true)
    public Page<Area> buscar(String query, int page, int size) {
        if (page < 0) page = 0;
        if (size <= 0) size = 5;

        Pageable pageable = PageRequest.of(page, size);

        if (query == null || query.trim().isEmpty()) {
            return super.paginar(page, size);
        }

        return areaRepository.findByNombreContainingIgnoreCase(query.trim(), pageable);
    }

}
