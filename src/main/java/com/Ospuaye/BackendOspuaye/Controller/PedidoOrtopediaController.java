package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Documento;
import com.Ospuaye.BackendOspuaye.Entity.PedidoOrtopedia;
import com.Ospuaye.BackendOspuaye.Service.DocumentoService;
import com.Ospuaye.BackendOspuaye.Service.PedidoOrtopediaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
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
            return ResponseEntity.ok(service.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> crearPedido(
            @RequestPart("pedido") String pedidoJson,
            @RequestPart(value = "documentos", required = false) List<MultipartFile> files
    ) {
        try {
            PedidoOrtopedia pedido = objectMapper.readValue(pedidoJson, PedidoOrtopedia.class);

            // Procesar documentos (opcional)
            List<Documento> documentos = new ArrayList<>();
            if (files != null) {
                for (MultipartFile file : files) {
                    String msg = documentoService.handleFileUpload(file);
                    if (!"Archivo cargado correctamente".equals(msg)) {
                        return ResponseEntity.badRequest().body(msg);
                    }
                    Documento doc = Documento.builder()
                            .nombreArchivo(file.getOriginalFilename())
                            .path("C://Ospuaye/documentos/" + file.getOriginalFilename())
                            .observacion("Estudio previo adjunto")
                            .build();
                    documentos.add(doc);
                }
            }

            var creado = service.crearPedido(pedido, documentos);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear el pedido: " + e.getMessage());
        }
    }

}
