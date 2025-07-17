package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import com.Ospuaye.BackendOspuaye.Repository.BeneficiarioRepository;
import org.springframework.stereotype.Service;

@Service
public class BeneficiarioService extends BaseService<Beneficiario, Long> {

    public BeneficiarioService(BeneficiarioRepository repository) {
        super(repository);
    }
}
