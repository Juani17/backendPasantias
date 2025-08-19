package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Documento;
import com.Ospuaye.BackendOspuaye.Entity.PedidoOftalmologia;
import com.Ospuaye.BackendOspuaye.Entity.Usuario;
import com.Ospuaye.BackendOspuaye.Service.DocumentoService;
import com.Ospuaye.BackendOspuaye.Service.PedidoOftalmologiaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos/oftalmologia")
@RequiredArgsConstructor
public class PedidoOftalmologiaController {

    private final PedidoOftalmologiaService service;
    private final DocumentoService documentoService;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> crearPedido(
            @RequestPart("pedido") String pedidoJson,
            @RequestPart("usuario") String usuarioJson,
            @RequestPart("documentos") List<MultipartFile> files
    ) {
        try {
            // Deserializar pedido y usuario
            PedidoOftalmologia pedido = objectMapper.readValue(pedidoJson, PedidoOftalmologia.class);
            Usuario usuario = objectMapper.readValue(usuarioJson, Usuario.class);

            // Procesar documentos subidos
            List<Documento> documentos = new ArrayList<>();
            for (MultipartFile file : files) {
                String msg = documentoService.handleFileUpload(file);
                if (!"Archivo cargado correctamente".equals(msg)) {
                    return ResponseEntity.badRequest().body(msg);
                }
                Documento doc = Documento.builder()
                        .nombreArchivo(file.getOriginalFilename())
                        .path("C://Ospuaye/documentos/" + file.getOriginalFilename())
                        .observacion("Estudio oftalmológico adjunto")
                        .build();
                documentos.add(doc);
            }

            var creado = service.crearPedido(pedido, documentos, usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear pedido oftalmología: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<PedidoOftalmologia>> listarTodos() {
        return ResponseEntity.ok(service.findAll());
    }
}
