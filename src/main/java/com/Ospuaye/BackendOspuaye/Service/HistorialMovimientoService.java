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
    public HistorialMovimiento crear(HistorialMovimiento entity) throws Exception {
        if (entity == null) throw new IllegalArgumentException("El historial de movimiento no puede ser nulo");

        if (entity.getUsuario() == null || entity.getUsuario().getId() == null)
            throw new IllegalArgumentException("El usuario asociado es obligatorio");
        Usuario u = usuarioRepository.findById(entity.getUsuario().getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        entity.setUsuario(u);

        if (entity.getPedido() == null || entity.getPedido().getId() == null)
            throw new IllegalArgumentException("El pedido asociado es obligatorio");
        Pedido p = pedidoRepository.findById(entity.getPedido().getId())
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));
        entity.setPedido(p);

        if (entity.getFecha() == null) entity.setFecha(new Date());
        if (entity.getEstado() == null)
            throw new IllegalArgumentException("El estado es obligatorio");

        return historialRepository.save(entity);
    }

    @Override
    @Transactional
    public HistorialMovimiento actualizar(HistorialMovimiento entity) throws Exception {
        if (entity == null || entity.getId() == null)
            throw new IllegalArgumentException("El historial o su ID no pueden ser nulos");

        HistorialMovimiento existente = historialRepository.findById(entity.getId())
                .orElseThrow(() -> new IllegalArgumentException("HistorialMovimiento no encontrado"));

        if (entity.getUsuario() != null && entity.getUsuario().getId() != null) {
            Usuario u = usuarioRepository.findById(entity.getUsuario().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            existente.setUsuario(u);
        }

        if (entity.getPedido() != null && entity.getPedido().getId() != null) {
            Pedido p = pedidoRepository.findById(entity.getPedido().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));
            existente.setPedido(p);
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
