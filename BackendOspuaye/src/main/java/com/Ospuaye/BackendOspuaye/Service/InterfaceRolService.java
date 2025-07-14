package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.InterfaceRol;
import com.Ospuaye.BackendOspuaye.Repository.InterfaceRolRepository;
import org.springframework.stereotype.Service;

@Service
public class InterfaceRolService extends BaseService<InterfaceRol, Long> {
    public InterfaceRolService(InterfaceRolRepository repository) {
        super(repository);
    }
}
