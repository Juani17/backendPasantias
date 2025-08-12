package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Dto.PedidoRequest;
import com.Ospuaye.BackendOspuaye.Entity.PedidoOrtopedia;
import com.Ospuaye.BackendOspuaye.Entity.Usuario;
import com.Ospuaye.BackendOspuaye.Service.DocumentoService;
import com.Ospuaye.BackendOspuaye.Service.PedidoOrtopediaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos/ortopedia")
@RequiredArgsConstructor
public class PedidoOrtopediaController {

    private final PedidoOrtopediaService service;
    private final DocumentoService documentoService;
    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<?> listarPedidos() {
        try {
            return ResponseEntity.ok(service.listar());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> crearPedido(
            @RequestPart("pedido") String pedidoJson,
            @RequestPart("usuario") String usuarioJson,
            @RequestPart("documentos") List<MultipartFile> files
    ) {
        try {
            PedidoOrtopedia pedido = objectMapper.readValue(pedidoJson, PedidoOrtopedia.class);
            Usuario usuario = objectMapper.readValue(usuarioJson, Usuario.class);

            List<com.Ospuaye.BackendOspuaye.Entity.Documento> documentos = new ArrayList<>();
            for (MultipartFile file : files) {
                String path = documentoService.handleFileUpload(file);
                com.Ospuaye.BackendOspuaye.Entity.Documento doc = com.Ospuaye.BackendOspuaye.Entity.Documento.builder()
                        .nombreArchivo(file.getOriginalFilename())
                        .path(path)
                        .observacion("Estudio previo adjunto")
                        .build();
                documentos.add(doc);
            }

            PedidoOrtopedia creado = service.crearPedido(pedido, documentos, usuario);
            return ResponseEntity.ok(creado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
