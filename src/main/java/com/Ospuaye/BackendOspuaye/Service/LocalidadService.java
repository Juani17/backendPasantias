package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Localidad;
import com.Ospuaye.BackendOspuaye.Entity.Departamento;
import com.Ospuaye.BackendOspuaye.Repository.LocalidadRepository;
import com.Ospuaye.BackendOspuaye.Repository.DepartamentoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LocalidadService extends BaseNombrableService<Localidad, Long> {

    private final LocalidadRepository localidadRepository;
    private final DepartamentoRepository departamentoRepository;

    public LocalidadService(LocalidadRepository localidadRepository,
                            DepartamentoRepository departamentoRepository) {
        super(localidadRepository);
        this.localidadRepository = localidadRepository;
        this.departamentoRepository = departamentoRepository;
    }

    @Transactional(readOnly = true)
    public Page<Localidad> buscar(String query, int page, int size) {
        if (page < 0) page = 0;
        if (size <= 0) size = 5;

        Pageable pageable = PageRequest.of(page, size);

        if (query == null || query.trim().isEmpty()) {
            return super.paginar(page, size);
        }

        String q = query.trim();

        return localidadRepository.findByNombreContainingIgnoreCaseOrCodigoPostalContainingIgnoreCase(q, q, pageable);
    }

    @Override
    @Transactional
    public Localidad crear(Localidad entity) throws Exception {
        if (entity == null) throw new IllegalArgumentException("La localidad no puede ser nula");
        if (entity.getNombre() == null || entity.getNombre().isEmpty())
            throw new IllegalArgumentException("El nombre de la localidad es obligatorio");
        if (entity.getDepartamento() == null || entity.getDepartamento().getId() == null)
            throw new IllegalArgumentException("El departamento es obligatorio");

        Departamento dep = departamentoRepository.findById(entity.getDepartamento().getId())
                .orElseThrow(() -> new IllegalArgumentException("Departamento no encontrado"));

        if (localidadRepository.existsByNombreAndDepartamento_Id(entity.getNombre(), dep.getId()))
            throw new IllegalArgumentException("Ya existe una localidad con ese nombre en el departamento");

        entity.setDepartamento(dep);
        if (entity.getActivo() == null) entity.setActivo(true);

        return localidadRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<Localidad> listarActivas() {
        return localidadRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public List<Localidad> listarPorDepartamento(Long departamentoId) throws Exception {
        if (departamentoId == null) throw new IllegalArgumentException("ID de departamento no puede ser nulo");
        Departamento dep = departamentoRepository.findById(departamentoId)
                .orElseThrow(() -> new IllegalArgumentException("Departamento no encontrado"));
        return localidadRepository.findByDepartamento(dep);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public Optional<Localidad> listarPorNombre(String nombre) throws Exception {
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");


        Optional<Localidad> localidades = localidadRepository.findByNombre(nombre);

        if (localidades.isEmpty())
            throw new IllegalArgumentException("No se encontraron localidades con ese nombre en el departamento");

        return localidades;
    }

}
