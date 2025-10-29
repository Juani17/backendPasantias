package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Nacionalidad;
import com.Ospuaye.BackendOspuaye.Repository.NacionalidadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NacionalidadService extends BaseService<Nacionalidad, Long> {

    private final NacionalidadRepository nacionalidadRepository;

    public NacionalidadService(NacionalidadRepository nacionalidadRepository) {
        super(nacionalidadRepository);
        this.nacionalidadRepository = nacionalidadRepository;
    }

    @Override
    @Transactional
    public Nacionalidad crear(Nacionalidad entity) throws Exception {
        if (entity == null) throw new IllegalArgumentException("Nacionalidad no puede ser nula");

        if (entity.getNombre() == null || entity.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre de la nacionalidad es obligatorio");

        // Validar duplicados
        if (nacionalidadRepository.findByNombre(entity.getNombre()).isPresent())
            throw new IllegalArgumentException("La nacionalidad ya existe");

        if (entity.getActivo() == null) entity.setActivo(true);

        return nacionalidadRepository.save(entity);
    }

    @Override
    @Transactional
    public Nacionalidad actualizar(Nacionalidad entity) throws Exception {
        if (entity == null || entity.getId() == null)
            throw new IllegalArgumentException("Nacionalidad o su ID no pueden ser nulos");

        Nacionalidad existente = nacionalidadRepository.findById(entity.getId())
                .orElseThrow(() -> new IllegalArgumentException("Nacionalidad no encontrada"));

        if (entity.getNombre() != null && !entity.getNombre().isBlank()) {
            var duplicado = nacionalidadRepository.findByNombre(entity.getNombre());
            if (duplicado.isPresent() && !duplicado.get().getId().equals(entity.getId()))
                throw new IllegalArgumentException("El nombre de la nacionalidad ya está en uso");
            existente.setNombre(entity.getNombre());
        }

        if (entity.getActivo() != null) existente.setActivo(entity.getActivo());

        return nacionalidadRepository.save(existente);
    }

    @Transactional(readOnly = true)
    public List<Nacionalidad> listarActivas() {
        return nacionalidadRepository.findAll()
                .stream()
                .filter(n -> Boolean.TRUE.equals(n.getActivo()))
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<Nacionalidad> ListarPorNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre de la Nacionalidad no puede ser nulo o vacio");
        }
        Optional<Nacionalidad> nacionalidad = nacionalidadRepository.findByNombre(nombre);

        if (nacionalidad.isEmpty()) {
            throw new IllegalArgumentException("No se encontro una nacionalidad con ese nombre");
        }
        return nacionalidad;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public Optional<Nacionalidad> ListarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id de la Nacionalidad no puede ser nulo o vacio");
        }
        Optional<Nacionalidad> nacionalidad = nacionalidadRepository.findById(id);

        if (nacionalidad.isEmpty()) {
            throw new IllegalArgumentException("No se encontro una nacionalidad con ese id");
        }
        return nacionalidad;
    }
}
