package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Domicilio;
import com.Ospuaye.BackendOspuaye.Entity.Localidad;
import com.Ospuaye.BackendOspuaye.Repository.DomicilioRepository;
import com.Ospuaye.BackendOspuaye.Repository.LocalidadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DomicilioService extends BaseService<Domicilio, Long> {

    private final DomicilioRepository domicilioRepository;
    private final LocalidadRepository localidadRepository;

    public DomicilioService(DomicilioRepository domicilioRepository,
                            LocalidadRepository localidadRepository) {
        super(domicilioRepository);
        this.domicilioRepository = domicilioRepository;
        this.localidadRepository = localidadRepository;
    }

    @Override
    @Transactional
    public Domicilio crear(Domicilio entity) throws Exception {
        if (entity == null) throw new IllegalArgumentException("El domicilio no puede ser nulo");

        if (entity.getLocalidad() != null) {
            Long locId = entity.getLocalidad().getId();
            if (locId == null) throw new IllegalArgumentException("La localidad debe tener ID");
            if (!localidadRepository.existsById(locId))
                throw new IllegalArgumentException("La localidad indicada no existe");
        }

        if (entity.getActivo() == null) entity.setActivo(true);
        return domicilioRepository.save(entity);
    }

    @Override
    @Transactional
    public Domicilio actualizar(Domicilio entity) throws Exception {
        if (entity == null || entity.getId() == null)
            throw new IllegalArgumentException("El domicilio o su ID no pueden ser nulos");

        Domicilio existente = domicilioRepository.findById(entity.getId())
                .orElseThrow(() -> new IllegalArgumentException("Domicilio no encontrado"));

        existente.setCalle(entity.getCalle());
        existente.setNumeracion(entity.getNumeracion());
        existente.setBarrio(entity.getBarrio());
        existente.setManzanaPiso(entity.getManzanaPiso());
        existente.setCasaDepartamento(entity.getCasaDepartamento());
        existente.setReferencia(entity.getReferencia());
        existente.setTipo(entity.getTipo());
        if (entity.getActivo() != null) existente.setActivo(entity.getActivo());

        if (entity.getLocalidad() != null) {
            Long locId = entity.getLocalidad().getId();
            if (locId == null || !localidadRepository.existsById(locId))
                throw new IllegalArgumentException("La localidad indicada no existe");
            existente.setLocalidad(entity.getLocalidad());
        }

        return domicilioRepository.save(existente);
    }

    @Transactional(readOnly = true)
    public List<Domicilio> listarPorLocalidad(Long localidadId) throws Exception {
        if (localidadId == null) throw new IllegalArgumentException("El ID de localidad es obligatorio");
        Localidad loc = localidadRepository.findById(localidadId)
                .orElseThrow(() -> new IllegalArgumentException("Localidad no encontrada"));
        return domicilioRepository.findByLocalidad(loc);
    }

    @Transactional(readOnly = true)
    public List<Domicilio> listarActivos() {
        return domicilioRepository.findByActivoTrue();
    }
}
