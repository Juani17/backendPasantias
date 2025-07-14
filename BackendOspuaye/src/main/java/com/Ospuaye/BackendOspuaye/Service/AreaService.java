package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Area;
import com.Ospuaye.BackendOspuaye.Repository.AreaRepository;
import org.springframework.stereotype.Service;

@Service
public class AreaService extends BaseService<Area, Long> {
    public AreaService(AreaRepository repository) {
        super(repository);
    }
}
