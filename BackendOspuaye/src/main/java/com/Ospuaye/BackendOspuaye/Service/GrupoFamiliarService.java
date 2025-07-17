package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.GrupoFamiliar;
import com.Ospuaye.BackendOspuaye.Repository.GrupoFamiliarRepository;
import org.springframework.stereotype.Service;

@Service
public class GrupoFamiliarService extends BaseService<GrupoFamiliar, Long> {

    public GrupoFamiliarService(GrupoFamiliarRepository repository) {
        super(repository);
    }
}
