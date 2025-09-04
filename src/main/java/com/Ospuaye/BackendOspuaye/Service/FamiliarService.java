package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.*;
import com.Ospuaye.BackendOspuaye.Repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FamiliarService extends BaseService<Familiar, Long> {

    private final FamiliarRepository familiarRepository;
    private final GrupoFamiliarRepository grupoFamiliarRepository;
    private final BeneficiarioRepository beneficiarioRepository;
    private final PersonaRepository personaRepository;

    public FamiliarService(FamiliarRepository familiarRepository,
                           GrupoFamiliarRepository grupoFamiliarRepository,
                           BeneficiarioRepository beneficiarioRepository,
                           PersonaRepository personaRepository) {
        super(familiarRepository);
        this.familiarRepository = familiarRepository;
        this.grupoFamiliarRepository = grupoFamiliarRepository;
        this.beneficiarioRepository = beneficiarioRepository;
        this.personaRepository = personaRepository;
    }

    @Override
    @Transactional
    public Familiar crear(Familiar entity) throws Exception {
        if (entity == null) throw new IllegalArgumentException("El familiar no puede ser nulo");

        // persona obligatoria y única en familiares
        if (entity.getPersona() == null || entity.getPersona().getId() == null)
            throw new IllegalArgumentException("La persona asociada es obligatoria");
        if (!personaRepository.existsById(entity.getPersona().getId()))
            throw new IllegalArgumentException("La persona asociada no existe");
        Optional<Familiar> existente = familiarRepository.findByPersona_Id(entity.getPersona().getId());
        if (existente.isPresent())
            throw new IllegalArgumentException("Ya existe un familiar con esa persona");

        // beneficiario (opcional, pero si viene debe existir)
        if (entity.getBeneficiario() != null) {
            Long benId = entity.getBeneficiario().getId();
            if (benId == null || !beneficiarioRepository.existsById(benId))
                throw new IllegalArgumentException("Beneficiario no encontrado");
        }

        // grupo familiar (opcional, pero si viene debe existir)
        if (entity.getGrupoFamiliar() != null) {
            Long gfId = entity.getGrupoFamiliar().getId();
            if (gfId == null || !grupoFamiliarRepository.existsById(gfId))
                throw new IllegalArgumentException("Grupo familiar no encontrado");
        }

        return familiarRepository.save(entity);
    }

    @Override
    @Transactional
    public Familiar actualizar(Familiar entity) throws Exception {
        if (entity == null || entity.getId() == null)
            throw new IllegalArgumentException("El familiar o su ID no pueden ser nulos");

        Familiar existente = familiarRepository.findById(entity.getId())
                .orElseThrow(() -> new IllegalArgumentException("Familiar no encontrado"));

        if (entity.getPersona() != null) {
            Long personaId = entity.getPersona().getId();
            if (personaId == null || !personaRepository.existsById(personaId))
                throw new IllegalArgumentException("Persona no encontrada");
            Optional<Familiar> byPersona = familiarRepository.findByPersona_Id(personaId);
            if (byPersona.isPresent() && !byPersona.get().getId().equals(entity.getId()))
                throw new IllegalArgumentException("La persona ya está vinculada a otro familiar");
            existente.setPersona(entity.getPersona());
        }

        if (entity.getBeneficiario() != null) {
            Long benId = entity.getBeneficiario().getId();
            if (benId == null || !beneficiarioRepository.existsById(benId))
                throw new IllegalArgumentException("Beneficiario no encontrado");
            existente.setBeneficiario(entity.getBeneficiario());
        }

        if (entity.getGrupoFamiliar() != null) {
            Long gfId = entity.getGrupoFamiliar().getId();
            if (gfId == null || !grupoFamiliarRepository.existsById(gfId))
                throw new IllegalArgumentException("Grupo familiar no encontrado");
            existente.setGrupoFamiliar(entity.getGrupoFamiliar());
        }

        if (entity.getTipoParentesco() != null)
            existente.setTipoParentesco(entity.getTipoParentesco());

        return familiarRepository.save(existente);
    }

    @Transactional(readOnly = true)
    public List<Familiar> listarPorBeneficiario(Long beneficiarioId) throws Exception {
        if (beneficiarioId == null) throw new IllegalArgumentException("ID de beneficiario es obligatorio");
        Beneficiario b = beneficiarioRepository.findById(beneficiarioId)
                .orElseThrow(() -> new IllegalArgumentException("Beneficiario no encontrado"));
        return familiarRepository.findByBeneficiario(b);
    }

    @Transactional(readOnly = true)
    public List<Familiar> listarPorGrupoFamiliar(Long grupoFamiliarId) throws Exception {
        if (grupoFamiliarId == null) throw new IllegalArgumentException("ID de grupo familiar es obligatorio");
        GrupoFamiliar gf = grupoFamiliarRepository.findById(grupoFamiliarId)
                .orElseThrow(() -> new IllegalArgumentException("Grupo familiar no encontrado"));
        return familiarRepository.findByGrupoFamiliar(gf);
    }
}
