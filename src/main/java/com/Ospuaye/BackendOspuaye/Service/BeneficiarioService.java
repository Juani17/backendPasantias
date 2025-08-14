package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Dto.BeneficiarioDTO;
import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import com.Ospuaye.BackendOspuaye.Repository.BeneficiarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BeneficiarioService extends BaseService<Beneficiario, Long> {

    private final BeneficiarioRepository beneficiarioRepository;

    public BeneficiarioService(BeneficiarioRepository beneficiarioRepository) {
        super(beneficiarioRepository);
        this.beneficiarioRepository = beneficiarioRepository;
    }

    @Override
    @Transactional
    public Beneficiario crear(Beneficiario beneficiario) throws Exception {
        validarBeneficiario(beneficiario, true);
        return beneficiarioRepository.save(beneficiario);
    }

    @Override
    @Transactional
    public Beneficiario actualizar(Beneficiario beneficiario) throws Exception {
        if (beneficiario == null || beneficiario.getId() == null) {
            throw new Exception("ID del beneficiario es obligatorio para actualizar");
        }
        if (!beneficiarioRepository.existsById(beneficiario.getId())) {
            throw new Exception("Beneficiario no encontrado");
        }
        validarBeneficiario(beneficiario, false);
        return beneficiarioRepository.save(beneficiario);
    }

    @Override
    @Transactional
    public void eliminar(Long id) throws Exception {
        super.eliminar(id); // BaseService ya chequea existencia y nulos
    }

    public Optional<Beneficiario> buscarPorDni(String dni) throws Exception {
        if (dni == null || dni.isBlank()) {
            throw new Exception("DNI vacío");
        }
        try {
            Integer dniInt = Integer.valueOf(dni);
            return beneficiarioRepository.findByDni(dniInt);
        } catch (NumberFormatException nfe) {
            throw new Exception("DNI inválido");
        }
    }

    public boolean dniExiste(Integer dni) {
        return beneficiarioRepository.findByDni(dni).isPresent();
    }

    private void validarBeneficiario(Beneficiario b, boolean esNuevo) throws Exception {
        if (b == null) throw new Exception("Beneficiario no puede ser nulo");
        if (b.getNombre() == null || b.getNombre().trim().isEmpty()) throw new Exception("Nombre es obligatorio");
        if (b.getApellido() == null || b.getApellido().trim().isEmpty()) throw new Exception("Apellido es obligatorio");
        if (b.getDni() == null) throw new Exception("DNI es obligatorio");
        String dniStr = b.getDni().toString();
        if (dniStr.length() < 7 || dniStr.length() > 8) throw new Exception("DNI inválido (7-8 dígitos esperados)");

        if (esNuevo) {
            if (beneficiarioRepository.findByDni(b.getDni()).isPresent()) {
                throw new Exception("Ya existe un beneficiario con ese DNI");
            }
        } else {
            // si se está actualizando: si el dni ya pertenece a otro registro, invalidar
            Optional<Beneficiario> porDni = beneficiarioRepository.findByDni(b.getDni());
            if (porDni.isPresent() && !porDni.get().getId().equals(b.getId())) {
                throw new Exception("El DNI ya está en uso por otro beneficiario");
            }
        }

        // ejemplo adicional: telefono opcional pero si viene validar que tenga al menos 6 dígitos
        if (b.getTelefono() != null) {
            String tel = b.getTelefono().toString();
            if (tel.length() < 6) throw new Exception("Teléfono inválido");
        }
    }

    public List<BeneficiarioDTO> listarDTOs() {
        return beneficiarioRepository.findAll().stream()
                .map(BeneficiarioDTO::new)
                .collect(Collectors.toList());
    }
}
