package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Dto.PedidoRequest;
import com.Ospuaye.BackendOspuaye.Entity.PedidoOftalmologia;
import com.Ospuaye.BackendOspuaye.Service.PedidoOftalmologiaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos/oftalmologia")
public class PedidoOftalmologiaController {

    private final PedidoOftalmologiaService service;

    public PedidoOftalmologiaController(PedidoOftalmologiaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> crearPedido(@Valid @RequestBody PedidoRequest<PedidoOftalmologia> request) {
        try {
            var creado = service.crearPedido(request.getPedido(), request.getDocumentos(), request.getUsuario());
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<PedidoOftalmologia>> listarTodos() {
        return ResponseEntity.ok(service.findAll());
    }
}
