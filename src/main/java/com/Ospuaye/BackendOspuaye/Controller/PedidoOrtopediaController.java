package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Documento;
import com.Ospuaye.BackendOspuaye.Entity.PedidoOrtopedia;
import com.Ospuaye.BackendOspuaye.Service.DocumentoService;
import com.Ospuaye.BackendOspuaye.Service.PedidoOrtopediaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;



import java.util.List;

@RestController
@RequestMapping("/api/pedidos/ortopedia")
public class PedidoOrtopediaController extends PedidoController<PedidoOrtopedia, PedidoOrtopediaService> {

    public PedidoOrtopediaController(PedidoOrtopediaService service,
                                     DocumentoService documentoService,
                                     ObjectMapper objectMapper) {
        super(service, documentoService, objectMapper);
    }

    @Override
    protected PedidoOrtopedia crearPedidoEspecifico(PedidoOrtopedia pedido, List<Documento> documentos) throws Exception {
        return pedidoService.crearPedido(pedido, documentos);
    }

    @Override
    protected Class<PedidoOrtopedia> getPedidoClass() {
        return PedidoOrtopedia.class;
    }

    @Override
    protected String getObservacionDocumento() {
        return "Documento de ortopedia";
    }
}
