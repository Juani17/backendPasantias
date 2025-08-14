package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Documento;
import com.Ospuaye.BackendOspuaye.Entity.Estado;
import com.Ospuaye.BackendOspuaye.Entity.PedidoOrtopedia;
import com.Ospuaye.BackendOspuaye.Entity.Usuario;
import com.Ospuaye.BackendOspuaye.Repository.PedidoOrtopediaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PedidoOrtopediaService extends PedidoService<PedidoOrtopedia> {

    private final PedidoOrtopediaRepository pedidoOrtopediaRepository;

    public PedidoOrtopediaService(PedidoOrtopediaRepository pedidoOrtopediaRepository) {
        super(pedidoOrtopediaRepository);
        this.pedidoOrtopediaRepository = pedidoOrtopediaRepository;
    }

    @Transactional
    public PedidoOrtopedia crearPedido(PedidoOrtopedia pedido, List<Documento> documentos, Usuario usuario) throws Exception {
        if (pedido == null) throw new Exception("Pedido no puede ser nulo");
        if (usuario == null) throw new Exception("Usuario que crea el pedido es obligatorio");
        if (pedido.getNombre() == null || pedido.getNombre().trim().isEmpty())
            throw new Exception("El nombre del pedido es obligatorio");
        if (pedido.getPaciente() != null && (pedido.getPaciente().getId() == null)) {
            pedido.setPaciente(null);
        }

        pedido.setEstado(Estado.Pendiente);
        pedido.setFechaIngreso(new Date());

        PedidoOrtopedia guardado = pedidoOrtopediaRepository.save(pedido);
        agregarDocumentos(guardado, documentos, usuario);
        registrarMovimiento(guardado, Estado.Pendiente, usuario, "Pedido ortopedia creado");

        return guardado;
    }
}
