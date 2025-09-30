package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.*;
import com.Ospuaye.BackendOspuaye.Entity.Enum.Estado;
import com.Ospuaye.BackendOspuaye.Repository.HistorialMovimientoRepository;
import com.Ospuaye.BackendOspuaye.Repository.PedidoRepository;
import com.Ospuaye.BackendOspuaye.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class HistorialMovimientoService extends BaseService<HistorialMovimiento, Long> {

    private final HistorialMovimientoRepository historialRepository;
    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;

    public HistorialMovimientoService(HistorialMovimientoRepository historialRepository,
                                      UsuarioRepository usuarioRepository,
                                      PedidoRepository pedidoRepository) {
        super(historialRepository);
        this.historialRepository = historialRepository;
        this.usuarioRepository = usuarioRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    @Transactional
    public HistorialMovimiento crear(HistorialMovimiento entity) {
        if (entity == null) throw new IllegalArgumentException("El historial de movimiento no puede ser nulo");

        if (entity.getUsuario() == null || entity.getUsuario().getId() == null)
            throw new IllegalArgumentException("El usuario asociado es obligatorio");
        Optional<Usuario> uOpt = usuarioRepository.findById(entity.getUsuario().getId());
        if (!uOpt.isPresent()) throw new IllegalArgumentException("Usuario no encontrado");
        entity.setUsuario(uOpt.get());

        if (entity.getPedido() == null || entity.getPedido().getId() == null)
            throw new IllegalArgumentException("El pedido asociado es obligatorio");
        Optional<Pedido> pOpt = pedidoRepository.findById(entity.getPedido().getId());
        if (!pOpt.isPresent()) throw new IllegalArgumentException("Pedido no encontrado");
        entity.setPedido(pOpt.get());

        if (entity.getFecha() == null) entity.setFecha(new Date());
        if (entity.getEstado() == null) throw new IllegalArgumentException("El estado es obligatorio");

        return historialRepository.save(entity);
    }

    @Override
    @Transactional
    public HistorialMovimiento actualizar(HistorialMovimiento entity) {
        if (entity == null || entity.getId() == null)
            throw new IllegalArgumentException("El historial o su ID no pueden ser nulos");

        Optional<HistorialMovimiento> existOpt = historialRepository.findById(entity.getId());
        if (!existOpt.isPresent()) throw new IllegalArgumentException("HistorialMovimiento no encontrado");
        HistorialMovimiento existente = existOpt.get();

        if (entity.getUsuario() != null && entity.getUsuario().getId() != null) {
            Optional<Usuario> uOpt = usuarioRepository.findById(entity.getUsuario().getId());
            if (!uOpt.isPresent()) throw new IllegalArgumentException("Usuario no encontrado");
            existente.setUsuario(uOpt.get());
        }

        if (entity.getPedido() != null && entity.getPedido().getId() != null) {
            Optional<Pedido> pOpt = pedidoRepository.findById(entity.getPedido().getId());
            if (!pOpt.isPresent()) throw new IllegalArgumentException("Pedido no encontrado");
            existente.setPedido(pOpt.get());
        }

        if (entity.getEstado() != null) existente.setEstado(entity.getEstado());
        if (entity.getComentario() != null) existente.setComentario(entity.getComentario());
        if (entity.getFecha() != null) existente.setFecha(entity.getFecha());

        return historialRepository.save(existente);
    }

    @Transactional(readOnly = true)
    public List<HistorialMovimiento> listarPorUsuario(Usuario usuario) {
        if (usuario == null || usuario.getId() == null)
            throw new IllegalArgumentException("Usuario inválido");
        return historialRepository.findByUsuario(usuario);
    }

    @Transactional(readOnly = true)
    public List<HistorialMovimiento> listarPorPedido(Pedido pedido) {
        if (pedido == null || pedido.getId() == null)
            throw new IllegalArgumentException("Pedido inválido");
        return historialRepository.findByPedido(pedido);
    }

    @Transactional(readOnly = true)
    public List<HistorialMovimiento> listarPorEstado(Estado estado) {
        if (estado == null) throw new IllegalArgumentException("El estado es obligatorio");
        return historialRepository.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    public List<HistorialMovimiento> listarPorRangoFecha(Date inicio, Date fin) {
        if (inicio == null || fin == null) throw new IllegalArgumentException("Las fechas no pueden ser nulas");
        if (fin.before(inicio)) throw new IllegalArgumentException("La fecha fin no puede ser anterior a la fecha inicio");
        return historialRepository.findByFechaBetween(inicio, fin);
    }
}
