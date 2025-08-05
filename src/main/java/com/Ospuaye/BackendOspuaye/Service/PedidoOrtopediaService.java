package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Documento;
import com.Ospuaye.BackendOspuaye.Entity.Estado;
import com.Ospuaye.BackendOspuaye.Entity.PedidoOrtopedia;
import com.Ospuaye.BackendOspuaye.Entity.Usuario;
import com.Ospuaye.BackendOspuaye.Repository.BaseRepository;
import com.Ospuaye.BackendOspuaye.Repository.PedidoOrtopediaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PedidoOrtopediaService extends PedidoService<PedidoOrtopedia> {


    private final PedidoOrtopediaRepository pedidoOrtopediaRepository;

    @Autowired
    public PedidoOrtopediaService(PedidoOrtopediaRepository pedidoOrtopediaRepository, PedidoOrtopediaRepository pedidoOrtopediaRepository1) {
        super(pedidoOrtopediaRepository);
        this.pedidoOrtopediaRepository = pedidoOrtopediaRepository1;
    }

    @Transactional
    public PedidoOrtopedia crearPedido(PedidoOrtopedia pedido, List<Documento> documentos, Usuario usuario) {
        pedido.setEstado(Estado.Pendiente);
        pedido.setFechaIngreso(new Date());

        PedidoOrtopedia guardado = baseRepository.save(pedido);
        agregarDocumentos(guardado, documentos, usuario);
        registrarMovimiento(guardado, Estado.Pendiente, usuario, "Pedido creado");

        return guardado;
    }
}

