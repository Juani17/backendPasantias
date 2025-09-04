package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.MovimientoObraSocial;
import com.Ospuaye.BackendOspuaye.Entity.Persona;
import com.Ospuaye.BackendOspuaye.Repository.MovimientoObraSocialRepository;
import com.Ospuaye.BackendOspuaye.Repository.PersonaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class MovimientoObraSocialService extends BaseService<MovimientoObraSocial, Long> {

    private final MovimientoObraSocialRepository movimientoRepository;
    private final PersonaRepository personaRepository;

    public MovimientoObraSocialService(MovimientoObraSocialRepository movimientoRepository,
                                       PersonaRepository personaRepository) {
        super(movimientoRepository);
        this.movimientoRepository = movimientoRepository;
        this.personaRepository = personaRepository;
    }

    @Override
    @Transactional
    public MovimientoObraSocial crear(MovimientoObraSocial entity) throws Exception {
        if (entity == null) throw new IllegalArgumentException("MovimientoObraSocial no puede ser nulo");

        if (entity.getPersona() == null || entity.getPersona().getId() == null)
            throw new IllegalArgumentException("Persona asociada es obligatoria");

        if (!personaRepository.existsById(entity.getPersona().getId()))
            throw new IllegalArgumentException("Persona no encontrada");

        // Validación de fechas
        if (entity.getFechaDesde() == null)
            throw new IllegalArgumentException("Fecha desde es obligatoria");
        if (entity.getFechaHasta() != null && entity.getFechaHasta().before(entity.getFechaDesde()))
            throw new IllegalArgumentException("Fecha hasta no puede ser anterior a fecha desde");

        return movimientoRepository.save(entity);
    }

    @Transactional
    public MovimientoObraSocial actualizar(MovimientoObraSocial entity) throws Exception {
        if (entity == null || entity.getId() == null)
            throw new IllegalArgumentException("MovimientoObraSocial o su ID no pueden ser nulos");

        MovimientoObraSocial existente = movimientoRepository.findById(entity.getId())
                .orElseThrow(() -> new IllegalArgumentException("MovimientoObraSocial no encontrado"));

        if (entity.getPersona() != null && entity.getPersona().getId() != null) {
            if (!personaRepository.existsById(entity.getPersona().getId()))
                throw new IllegalArgumentException("Persona no encontrada");
            existente.setPersona(entity.getPersona());
        }

        if (entity.getFechaDesde() != null) existente.setFechaDesde(entity.getFechaDesde());
        if (entity.getFechaHasta() != null) existente.setFechaHasta(entity.getFechaHasta());
        if (entity.getObservacion() != null) existente.setObservacion(entity.getObservacion());

        return movimientoRepository.save(existente);
    }

    @Transactional(readOnly = true)
    public List<MovimientoObraSocial> listarPorPersona(Long personaId) throws Exception {
        if (personaId == null) throw new IllegalArgumentException("ID de persona no puede ser nulo");
        Persona p = personaRepository.findById(personaId)
                .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada"));
        return movimientoRepository.findByPersona(p);
    }

    @Transactional(readOnly = true)
    public List<MovimientoObraSocial> listarPorRangoFecha(Date inicio, Date fin) {
        return movimientoRepository.findByFechaDesdeBetween(inicio, fin);
    }
}
