package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Estado;
import com.Ospuaye.BackendOspuaye.Entity.HistorialMovimiento;
import com.Ospuaye.BackendOspuaye.Repository.HistorialMovimientoRepository;
import com.Ospuaye.BackendOspuaye.Repository.PedidoRepository;
import com.Ospuaye.BackendOspuaye.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class HistorialMovimientoService extends BaseService<HistorialMovimiento, Long> {

    private final HistorialMovimientoRepository repository;
    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;

    public HistorialMovimientoService(HistorialMovimientoRepository repository,
                                      PedidoRepository pedidoRepository,
                                      UsuarioRepository usuarioRepository) {
        super(repository);
        this.repository = repository;
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public HistorialMovimiento crear(HistorialMovimiento h) throws Exception {
        validar(h);
        if (h.getFecha() == null) h.setFecha(new Date());
        return repository.save(h);
    }

    @Override
    public HistorialMovimiento actualizar(HistorialMovimiento h) throws Exception {
        if (h.getId() == null || !repository.existsById(h.getId())) {
            throw new Exception("Movimiento no encontrado");
        }
        validar(h);
        return repository.save(h);
    }

    private void validar(HistorialMovimiento h) throws Exception {
        if (h.getTipoMovimiento() == null || h.getTipoMovimiento() == Estado.Leido) {
            // Leído puede quedar como estado de lectura, pero si no querés permitirlo en alta:
            // throw new Exception("El tipo de movimiento es obligatorio");
        }
        if (h.getUsuario() == null || h.getUsuario().getId() == null
                || !usuarioRepository.existsById(h.getUsuario().getId())) {
            throw new Exception("El usuario del movimiento no existe");
        }
        if (h.getPedido() == null || h.getPedido().getId() == null
                || !pedidoRepository.existsById(h.getPedido().getId())) {
            throw new Exception("El pedido del movimiento no existe");
        }
    }
}
