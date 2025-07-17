package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.PedidoOftalmologia;
import com.Ospuaye.BackendOspuaye.Service.PedidoOftalmologiaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos-oftalmologia")
public class PedidoOftalmologiaController extends BaseController<PedidoOftalmologia, Long> {
    public PedidoOftalmologiaController(PedidoOftalmologiaService service) {
        super(service);
    }
}
