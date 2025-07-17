package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.PedidoOrtopedia;
import com.Ospuaye.BackendOspuaye.Service.PedidoOrtopediaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos-ortopedia")
public class PedidoOrtopediaController extends BaseController<PedidoOrtopedia, Long> {
    public PedidoOrtopediaController(PedidoOrtopediaService service) {
        super(service);
    }
}
