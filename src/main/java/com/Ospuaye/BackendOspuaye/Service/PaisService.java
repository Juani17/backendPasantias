package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Pais;
import com.Ospuaye.BackendOspuaye.Repository.PaisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaisService extends BaseService<Pais, Long> {

    private final PaisRepository paisRepository;

    public PaisService(PaisRepository paisRepository) {
        super(paisRepository);
        this.paisRepository = paisRepository;
    }

    @Override
    @Transactional
    public Pais crear(Pais entity) throws Exception {
        if (entity == null)
            throw new IllegalArgumentException("El país no puede ser nulo");

        if (entity.getNombre() == null || entity.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre del país es obligatorio");

        // Validar que no exista otro país con el mismo nombre
        if (paisRepository.findByNombre(entity.getNombre()).isPresent())
            throw new IllegalArgumentException("Ya existe un país con ese nombre");

        if (entity.getActivo() == null) entity.setActivo(true);

        return paisRepository.save(entity);
    }

    @Override
    @Transactional
    public Pais actualizar(Pais entity) throws Exception {
        if (entity == null || entity.getId() == null)
            throw new IllegalArgumentException("El país o su ID no pueden ser nulos");

        Pais existente = paisRepository.findById(entity.getId())
                .orElseThrow(() -> new IllegalArgumentException("País no encontrado"));

        if (entity.getNombre() != null && !entity.getNombre().isBlank()) {
            // Validar que el nuevo nombre no esté en uso por otro país
            var mismoNombre = paisRepository.findByNombre(entity.getNombre());
            if (mismoNombre.isPresent() && !mismoNombre.get().getId().equals(entity.getId())) {
                throw new IllegalArgumentException("El nombre del país ya está en uso");
            }
            existente.setNombre(entity.getNombre());
        }

        if (entity.getActivo() != null) existente.setActivo(entity.getActivo());

        return paisRepository.save(existente);
    }

    @Transactional(readOnly = true)
    public List<Pais> listarActivos() {
        return paisRepository.findAll().stream()
                .filter(p -> Boolean.TRUE.equals(p.getActivo()))
                .toList();
    }
}
