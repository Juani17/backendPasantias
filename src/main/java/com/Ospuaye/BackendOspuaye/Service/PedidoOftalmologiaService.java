package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Documento;
import com.Ospuaye.BackendOspuaye.Entity.Estado;
import com.Ospuaye.BackendOspuaye.Entity.PedidoOftalmologia;
import com.Ospuaye.BackendOspuaye.Entity.Usuario;
import com.Ospuaye.BackendOspuaye.Repository.PedidoOftalmologiaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PedidoOftalmologiaService extends PedidoService<PedidoOftalmologia> {

    private final PedidoOftalmologiaRepository pedidoOftalmologiaRepository;

    public PedidoOftalmologiaService(PedidoOftalmologiaRepository pedidoOftalmologiaRepository) {
        super(pedidoOftalmologiaRepository);
        this.pedidoOftalmologiaRepository = pedidoOftalmologiaRepository;
    }

    @Transactional
    public PedidoOftalmologia crearPedido(PedidoOftalmologia pedido, List<Documento> documentos, Usuario usuario) throws Exception {
        if (pedido == null) throw new Exception("Pedido no puede ser nulo");
        if (usuario == null) throw new Exception("Usuario que crea el pedido es obligatorio");
        if (pedido.getNombre() == null || pedido.getNombre().trim().isEmpty())
            throw new Exception("El nombre del pedido es obligatorio");

        pedido.setEstado(Estado.Pendiente);
        pedido.setFechaIngreso(new Date());

        PedidoOftalmologia guardado = pedidoOftalmologiaRepository.save(pedido);
        agregarDocumentos(guardado, documentos, usuario);
        registrarMovimiento(guardado, Estado.Pendiente, usuario, "Pedido oftalmología creado");

        return guardado;
    }
}
