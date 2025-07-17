package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.PedidoOrtopedia;
import com.Ospuaye.BackendOspuaye.Repository.PedidoOrtopediaRepository;
import org.springframework.stereotype.Service;

@Service
public class PedidoOrtopediaService extends BaseService<PedidoOrtopedia, Long> {

    public PedidoOrtopediaService(PedidoOrtopediaRepository repository) {
        super(repository);
    }
}
