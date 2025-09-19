package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.*;
import com.Ospuaye.BackendOspuaye.Repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FamiliarService extends BaseService<Familiar, Long> {

    private final FamiliarRepository familiarRepository;
    private final GrupoFamiliarRepository grupoFamiliarRepository;
    private final BeneficiarioRepository beneficiarioRepository;

    public FamiliarService(FamiliarRepository familiarRepository,
                           GrupoFamiliarRepository grupoFamiliarRepository,
                           BeneficiarioRepository beneficiarioRepository) {
        super(familiarRepository);
        this.familiarRepository = familiarRepository;
        this.grupoFamiliarRepository = grupoFamiliarRepository;
        this.beneficiarioRepository = beneficiarioRepository;
    }

    @Override
    @Transactional
    public Familiar crear(Familiar entity) throws Exception {
        if (entity == null) throw new IllegalArgumentException("El familiar no puede ser nulo");

        // Validaciones obligatorias
        if (entity.getNombre() == null || entity.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio");
        if (entity.getApellido() == null || entity.getApellido().isBlank())
            throw new IllegalArgumentException("El apellido es obligatorio");
        if (entity.getDni() == null)
            throw new IllegalArgumentException("El DNI es obligatorio");
        if (entity.getCuil() == null)
            throw new IllegalArgumentException("El CUIL es obligatorio");
        if (entity.getSexo() == null)
            throw new IllegalArgumentException("El sexo es obligatorio");

        // Validar unicidad de DNI y CUIL
        if (familiarRepository.existsByDni(entity.getDni()))
            throw new IllegalArgumentException("Ya existe un familiar con ese DNI");
        if (familiarRepository.existsByCuil(entity.getCuil()))
            throw new IllegalArgumentException("Ya existe un familiar con ese CUIL");

        // beneficiario (opcional, pero si viene debe existir)
        if (entity.getBeneficiario() != null) {
            Long benId = entity.getBeneficiario().getId();
            Beneficiario b = beneficiarioRepository.findById(benId)
                    .orElseThrow(() -> new IllegalArgumentException("Beneficiario no encontrado"));
            entity.setBeneficiario(b);
        }

        // grupo familiar (opcional, pero si viene debe existir)
        if (entity.getGrupoFamiliar() != null) {
            Long gfId = entity.getGrupoFamiliar().getId();
            GrupoFamiliar gf = grupoFamiliarRepository.findById(gfId)
                    .orElseThrow(() -> new IllegalArgumentException("Grupo familiar no encontrado"));
            entity.setGrupoFamiliar(gf);
        }

        // Nacionalidad opcional, pero si viene, se asigna
        if (entity.getNacionalidad() != null) {
            // opcional: podrías validar si existe en la DB
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

        // Actualizar campos simples
        if (entity.getNombre() != null) existente.setNombre(entity.getNombre());
        if (entity.getApellido() != null) existente.setApellido(entity.getApellido());
        if (entity.getDni() != null) existente.setDni(entity.getDni());
        if (entity.getCuil() != null) existente.setCuil(entity.getCuil());
        if (entity.getTelefono() != null) existente.setTelefono(entity.getTelefono());
        if (entity.getCorreoElectronico() != null) existente.setCorreoElectronico(entity.getCorreoElectronico());
        if (entity.getSexo() != null) existente.setSexo(entity.getSexo());
        if (entity.getTipoParentesco() != null) existente.setTipoParentesco(entity.getTipoParentesco());
        if (entity.getNacionalidad() != null) existente.setNacionalidad(entity.getNacionalidad());

        // Actualizar beneficiario si viene
        if (entity.getBeneficiario() != null) {
            Long benId = entity.getBeneficiario().getId();
            Beneficiario b = beneficiarioRepository.findById(benId)
                    .orElseThrow(() -> new IllegalArgumentException("Beneficiario no encontrado"));
            existente.setBeneficiario(b);
        }

        // Actualizar grupo familiar si viene
        if (entity.getGrupoFamiliar() != null) {
            Long gfId = entity.getGrupoFamiliar().getId();
            GrupoFamiliar gf = grupoFamiliarRepository.findById(gfId)
                    .orElseThrow(() -> new IllegalArgumentException("Grupo familiar no encontrado"));
            existente.setGrupoFamiliar(gf);
        }

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
