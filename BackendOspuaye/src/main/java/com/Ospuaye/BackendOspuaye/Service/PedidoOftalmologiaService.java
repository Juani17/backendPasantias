package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.PedidoOftalmologia;
import com.Ospuaye.BackendOspuaye.Repository.PedidoOftalmologiaRepository;
import org.springframework.stereotype.Service;

@Service
public class PedidoOftalmologiaService extends BaseService<PedidoOftalmologia, Long> {

    public PedidoOftalmologiaService(PedidoOftalmologiaRepository repository) {
        super(repository);
    }
}
