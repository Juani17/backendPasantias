package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import com.Ospuaye.BackendOspuaye.Repository.BeneficiarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BeneficiarioService extends BaseService<Beneficiario, Long> {

    private final BeneficiarioRepository beneficiarioRepository;

    public BeneficiarioService(BeneficiarioRepository beneficiarioRepository) {
        super(beneficiarioRepository); // ✅ llama al constructor de BaseService
        this.beneficiarioRepository = beneficiarioRepository;
    }

    @Override
    public List<Beneficiario> listar() {
        return beneficiarioRepository.findAll();
    }

    @Override
    public Optional<Beneficiario> buscarPorId(Long id) {
        return beneficiarioRepository.findById(id);
    }

    public Optional<Beneficiario> buscarPorDni(String dni) {
        return beneficiarioRepository.findByDni(Integer.valueOf(dni));
    }

    public boolean dniExiste(String dni) {
        return beneficiarioRepository.findByDni(Integer.valueOf(dni)).isPresent();
    }

    @Override
    public Beneficiario crear(Beneficiario beneficiario) {
        return beneficiarioRepository.save(beneficiario);
    }

    @Override
    public Beneficiario actualizar(Beneficiario beneficiario) {
        return beneficiarioRepository.save(beneficiario);
    }

    @Override
    public void eliminar(Long id) {
        beneficiarioRepository.deleteById(id);
    }
}
