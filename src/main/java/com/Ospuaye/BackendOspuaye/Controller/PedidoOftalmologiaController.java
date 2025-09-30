package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Documento;
import com.Ospuaye.BackendOspuaye.Entity.PedidoOftalmologia;
import com.Ospuaye.BackendOspuaye.Service.DocumentoService;
import com.Ospuaye.BackendOspuaye.Service.PedidoOftalmologiaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos/Oftalmologia")
public class PedidoOftalmologiaController extends PedidoController<PedidoOftalmologia, PedidoOftalmologiaService> {

    public PedidoOftalmologiaController(PedidoOftalmologiaService service,
                                        DocumentoService documentoService,
                                        ObjectMapper objectMapper) {
        super(service, documentoService, objectMapper);
    }

    @Override
    protected PedidoOftalmologia crearPedidoEspecifico(PedidoOftalmologia pedido, List<Documento> documentos) throws Exception {
        return pedidoService.crearPedido(pedido, documentos);
    }

    @Override
    protected Class<PedidoOftalmologia> getPedidoClass() {
        return PedidoOftalmologia.class;
    }

    @Override
    protected String getObservacionDocumento() {
        return "Documento de oftalmología";
    }
}

