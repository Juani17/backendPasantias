package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Pedido;
import com.Ospuaye.BackendOspuaye.Repository.PedidoRepository;
import org.springframework.stereotype.Service;

@Service
public class PedidoService extends BaseService<Pedido, Long> {

    public PedidoService(PedidoRepository repository) {
        super(repository);
    }
}
