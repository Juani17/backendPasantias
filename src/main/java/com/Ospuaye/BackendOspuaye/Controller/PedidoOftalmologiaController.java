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
        try {
            PedidoOftalmologia creado = service.crearPedido(request.getPedido(), request.getDocumentos(), request.getUsuario());
            return ResponseEntity.ok(creado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            List<PedidoOftalmologia> pedidos = service.findAll();
            return ResponseEntity.ok(pedidos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
