package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Localidad;
import com.Ospuaye.BackendOspuaye.Entity.Departamento;
import com.Ospuaye.BackendOspuaye.Repository.LocalidadRepository;
import com.Ospuaye.BackendOspuaye.Repository.DepartamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LocalidadService extends BaseService<Localidad, Long> {

    private final LocalidadRepository localidadRepository;
    private final DepartamentoRepository departamentoRepository;

    public LocalidadService(LocalidadRepository localidadRepository,
                            DepartamentoRepository departamentoRepository) {
        super(localidadRepository);
        this.localidadRepository = localidadRepository;
        this.departamentoRepository = departamentoRepository;
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
}
