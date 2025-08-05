package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.HistorialMovimiento;
import com.Ospuaye.BackendOspuaye.Repository.HistorialMovimientoRepository;
import org.springframework.stereotype.Service;

@Service
public class HistorialMovimientoService extends BaseService<HistorialMovimiento, Long> {

    public HistorialMovimientoService(HistorialMovimientoRepository repository) {
        super(repository);
    }
}
