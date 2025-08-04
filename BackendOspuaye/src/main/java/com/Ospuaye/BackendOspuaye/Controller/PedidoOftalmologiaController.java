package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Dto.PedidoRequest;
import com.Ospuaye.BackendOspuaye.Entity.PedidoOftalmologia;
import com.Ospuaye.BackendOspuaye.Service.PedidoOftalmologiaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos/oftalmologia")
public class PedidoOftalmologiaController {

    private final PedidoOftalmologiaService service;

    public PedidoOftalmologiaController(PedidoOftalmologiaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody PedidoRequest<PedidoOftalmologia> request) {
        PedidoOftalmologia creado = service.crearPedido(request.getPedido(), request.getDocumentos(), request.getUsuario());
        return ResponseEntity.ok(creado);
    }

    @GetMapping
    public ResponseEntity<List<PedidoOftalmologia>> listarTodos() {
        List<PedidoOftalmologia> pedidos = service.findAll();
        return ResponseEntity.ok(pedidos);
    }
}
