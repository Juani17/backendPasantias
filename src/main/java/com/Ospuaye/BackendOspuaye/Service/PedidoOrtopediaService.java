package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.*;
import com.Ospuaye.BackendOspuaye.Repository.PedidoOrtopediaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PedidoOrtopediaService extends PedidoService<PedidoOrtopedia> {

    private final PedidoOrtopediaRepository repo;

    public PedidoOrtopediaService(PedidoOrtopediaRepository pedidoOrtopediaRepository) {
        super(pedidoOrtopediaRepository);
        this.repo = pedidoOrtopediaRepository;
    }

    @Transactional
    public PedidoOrtopedia crearPedido(PedidoOrtopedia pedido,
                                       List<Documento> documentos,
                                       Usuario usuario) {
        try {
            validarPedidoComun(pedido);
            if (pedido.getMotivoConsulta() == null || pedido.getMotivoConsulta().isBlank())
                throw new Exception("El motivo de consulta es obligatorio");
            if (pedido.getRecetaMedica() == null)
                throw new Exception("Debe indicar si adjunta receta");

            pedido.setEstado(Estado.Pendiente);
            pedido.setFechaIngreso(new Date());

            PedidoOrtopedia guardado = baseRepository.save(pedido);
            agregarDocumentos(guardado, documentos, usuario);
            registrarMovimiento(guardado, Estado.Pendiente, usuario, "Pedido creado");

            return guardado;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
