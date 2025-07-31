package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Familiar;
import com.Ospuaye.BackendOspuaye.Repository.FamiliarRepository;
import org.springframework.stereotype.Service;

@Service
public class FamiliarService extends BaseService<Familiar,Long>{
    public FamiliarService(FamiliarRepository repository) {
        super(repository);
    }
}
