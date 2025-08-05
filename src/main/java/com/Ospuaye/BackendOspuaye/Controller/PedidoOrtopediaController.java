package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Dto.PedidoRequest;
import com.Ospuaye.BackendOspuaye.Entity.PedidoOrtopedia;
import com.Ospuaye.BackendOspuaye.Service.PedidoOrtopediaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos/ortopedia")
public class PedidoOrtopediaController {

    private final PedidoOrtopediaService service;

    public PedidoOrtopediaController(PedidoOrtopediaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody PedidoRequest<PedidoOrtopedia> request) {
        PedidoOrtopedia creado = service.crearPedido(request.getPedido(), request.getDocumentos(), request.getUsuario());
        return ResponseEntity.ok(creado);
    }

    @GetMapping
    public ResponseEntity<List<PedidoOrtopedia>> listarTodos() {
        List<PedidoOrtopedia> pedidos = service.findAll();
        return ResponseEntity.ok(pedidos);
    }
}
