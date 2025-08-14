package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.HistorialMovimiento;
import com.Ospuaye.BackendOspuaye.Repository.HistorialMovimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HistorialMovimientoService extends BaseService<HistorialMovimiento, Long> {

    private final HistorialMovimientoRepository repository;

    public HistorialMovimientoService(HistorialMovimientoRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    @Transactional
    public HistorialMovimiento crear(HistorialMovimiento h) throws Exception {
        if (h == null) throw new Exception("HistorialMovimiento no puede ser nulo");
        if (h.getPedido() == null || h.getTipoMovimiento() == null || h.getUsuario() == null)
            throw new Exception("Pedido, tipoMovimiento y usuario son obligatorios para el historial");
        return repository.save(h);
    }
}
