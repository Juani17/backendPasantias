package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.*;
import com.Ospuaye.BackendOspuaye.Repository.PedidoOftalmologiaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PedidoOftalmologiaService extends PedidoService<PedidoOftalmologia> {

    private final PedidoOftalmologiaRepository repo;

    public PedidoOftalmologiaService(PedidoOftalmologiaRepository pedidoOftalmologiaRepository) {
        super(pedidoOftalmologiaRepository);
        this.repo = pedidoOftalmologiaRepository;
    }

    @Transactional
    public PedidoOftalmologia crearPedido(PedidoOftalmologia pedido,
                                          List<Documento> documentos,
                                          Usuario usuario) {
        try {
            validarPedidoComun(pedido);
            if (pedido.getMotivoConsulta() == null || pedido.getMotivoConsulta().isBlank())
                throw new Exception("El motivo de consulta es obligatorio");
            if (pedido.getUsaLentes() == null)
                throw new Exception("Debe indicar si usa lentes");
            if (pedido.getRecetaMedica() == null)
                throw new Exception("Debe indicar si adjunta receta");

            pedido.setEstado(Estado.Pendiente);
            pedido.setFechaIngreso(new Date());

            PedidoOftalmologia guardado = baseRepository.save(pedido);
            agregarDocumentos(guardado, documentos, usuario);
            registrarMovimiento(guardado, Estado.Pendiente, usuario, "Pedido creado");
            return guardado;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
