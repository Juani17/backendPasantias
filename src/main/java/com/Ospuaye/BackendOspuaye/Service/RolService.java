package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Rol;
import com.Ospuaye.BackendOspuaye.Repository.RolRepository;
import org.springframework.stereotype.Service;

@Service
public class RolService extends BaseService<Rol, Long> {

    public RolService(RolRepository repository) {
        super(repository);
    }
}