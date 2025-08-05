package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Medico;
import com.Ospuaye.BackendOspuaye.Repository.MedicoRepository;
import org.springframework.stereotype.Service;

@Service
public class MedicoService extends BaseService<Medico, Long> {

    public MedicoService(MedicoRepository repository) {
        super(repository);
    }
}
