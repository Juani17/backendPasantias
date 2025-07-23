package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import com.Ospuaye.BackendOspuaye.Repository.BeneficiarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BeneficiarioService {

    private final BeneficiarioRepository beneficiarioRepository;

    public BeneficiarioService(BeneficiarioRepository beneficiarioRepository) {
        this.beneficiarioRepository = beneficiarioRepository;
    }

    public List<Beneficiario> listar() {
        return beneficiarioRepository.findAll();
    }

    public Optional<Beneficiario> buscarPorId(Long id) {
        return beneficiarioRepository.findById(id);
    }

    public Optional<Beneficiario> buscarPorDni(String dni) {
        return beneficiarioRepository.findByDni(dni);
    }

    public boolean dniExiste(String dni) {
        return beneficiarioRepository.findByDni(dni).isPresent();
    }

    public Beneficiario crear(Beneficiario beneficiario) {
        return beneficiarioRepository.save(beneficiario);
    }

    public Beneficiario actualizar(Beneficiario beneficiario) {
        return beneficiarioRepository.save(beneficiario);
    }

    public void eliminar(Long id) {
        beneficiarioRepository.deleteById(id);
    }
}
