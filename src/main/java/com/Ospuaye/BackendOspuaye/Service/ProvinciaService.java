package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Nacionalidad;
import com.Ospuaye.BackendOspuaye.Entity.Pais;
import com.Ospuaye.BackendOspuaye.Entity.Provincia;
import com.Ospuaye.BackendOspuaye.Repository.ProvinciaRepository;
import com.Ospuaye.BackendOspuaye.Repository.PaisRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProvinciaService extends BaseService<Provincia, Long> {

    private final ProvinciaRepository provinciaRepository;
    private final PaisRepository paisRepository;

    public ProvinciaService(ProvinciaRepository provinciaRepository, PaisRepository paisRepository) {
        super(provinciaRepository);
        this.provinciaRepository = provinciaRepository;
        this.paisRepository = paisRepository;
    }

    // ===============================================
    // BUSQUEDA GLOBAL + PAGINADO (ACTIVOS)
    // ===============================================
    @Transactional(readOnly = true)
    public Page<Provincia> buscar(String query, int page, int size) {
        if (page < 0) page = 0;
        if (size <= 0) size = 5;

        Pageable pageable = PageRequest.of(page, size);

        if (query == null || query.trim().isEmpty()) {
            return super.paginar(page, size); // ✅ activos por defecto
        }

        String q = query.trim();
        return provinciaRepository.findByNombreContainingIgnoreCaseAndActivoTrueOrPais_NombreContainingIgnoreCaseAndActivoTrue(
                q, q, pageable);
    }

    // ===============================================
    // BUSQUEDA GLOBAL + PAGINADO (INACTIVOS)
    // ===============================================
    @Transactional(readOnly = true)
    public Page<Provincia> buscarInactivos(String query, int page, int size) {
        if (page < 0) page = 0;
        if (size <= 0) size = 5;

        Pageable pageable = PageRequest.of(page, size);

        if (query == null || query.trim().isEmpty()) {
            return super.paginarInactivos(page, size); // ✅ inactivos por defecto
        }

        String q = query.trim();
        return provinciaRepository.findByNombreContainingIgnoreCaseAndActivoFalseOrPais_NombreContainingIgnoreCaseAndActivoFalse(
                q, q, pageable);
    }


    @Override
    @Transactional
    public Provincia crear(Provincia entity) throws Exception {
        if (entity == null) throw new IllegalArgumentException("Provincia no puede ser nula");

        if (entity.getNombre() == null || entity.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre de la provincia es obligatorio");

        if (provinciaRepository.findByNombre(entity.getNombre()).isPresent())
            throw new IllegalArgumentException("Ya existe una provincia con este nombre");

        if (entity.getPais() == null || entity.getPais().getId() == null)
            throw new IllegalArgumentException("El país asociado es obligatorio");

        Pais p = paisRepository.findById(entity.getPais().getId())
                .orElseThrow(() -> new IllegalArgumentException("País no encontrado"));
        entity.setPais(p);

        if (entity.getActivo() == null) entity.setActivo(true);

        return provinciaRepository.save(entity);
    }

    @Override
    @Transactional
    public Provincia actualizar(Provincia entity) throws Exception {
        if (entity == null || entity.getId() == null)
            throw new IllegalArgumentException("Provincia o su ID no pueden ser nulos");

        Provincia existente = provinciaRepository.findById(entity.getId())
                .orElseThrow(() -> new IllegalArgumentException("Provincia no encontrada"));

        if (entity.getNombre() != null && !entity.getNombre().isBlank()) {
            var porNombre = provinciaRepository.findByNombre(entity.getNombre());
            if (porNombre.isPresent() && !porNombre.get().getId().equals(entity.getId())) {
                throw new IllegalArgumentException("Ya existe otra provincia con este nombre");
            }
            existente.setNombre(entity.getNombre());
        }

        if (entity.getPais() != null && entity.getPais().getId() != null) {
            Pais p = paisRepository.findById(entity.getPais().getId())
                    .orElseThrow(() -> new IllegalArgumentException("País no encontrado"));
            existente.setPais(p);
        }

        if (entity.getActivo() != null) existente.setActivo(entity.getActivo());

        return provinciaRepository.save(existente);
    }

    @Transactional(readOnly = true)
    public Optional<Provincia> ListarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id de la provincia no puede ser nulo o vacio");
        }
        Optional<Provincia> provincia = provinciaRepository.findById(id);

        if (provincia.isEmpty()) {
            System.out.println("s" + id);
            throw new IllegalArgumentException("No se encontro una provincia con ese id");
        }
        return provincia;
    }

    @Transactional(readOnly = true)
    public List<Provincia> buscarPorNombre(String nombre) {

        if (nombre == null || nombre.trim().isEmpty()) {
            return List.of();
        }

        return provinciaRepository.findByNombreContainingIgnoreCase(nombre.trim());
    }
}


