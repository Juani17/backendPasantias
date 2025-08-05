package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Documento;
import com.Ospuaye.BackendOspuaye.Entity.Estado;
import com.Ospuaye.BackendOspuaye.Entity.PedidoOftalmologia;
import com.Ospuaye.BackendOspuaye.Entity.Usuario;
import com.Ospuaye.BackendOspuaye.Repository.PedidoOftalmologiaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PedidoOftalmologiaService extends PedidoService<PedidoOftalmologia> {

    private final PedidoOftalmologiaRepository pedidoOftalmologiaRepository;

    @Autowired
    public PedidoOftalmologiaService(PedidoOftalmologiaRepository pedidoOftalmologiaRepository) {
        super(pedidoOftalmologiaRepository);
        this.pedidoOftalmologiaRepository = pedidoOftalmologiaRepository;
    }

    @Transactional
    public PedidoOftalmologia crearPedido(PedidoOftalmologia pedido, List<Documento> documentos, Usuario usuario) {
        pedido.setEstado(Estado.Pendiente);
        pedido.setFechaIngreso(new Date());

        PedidoOftalmologia guardado = baseRepository.save(pedido);
        agregarDocumentos(guardado, documentos, usuario);
        registrarMovimiento(guardado, Estado.Pendiente, usuario, "Pedido creado");

        return guardado;
    }
}
