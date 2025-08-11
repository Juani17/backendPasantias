package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Dto.PedidoRequest;
import com.Ospuaye.BackendOspuaye.Entity.Documento;
import com.Ospuaye.BackendOspuaye.Entity.PedidoOrtopedia;
import com.Ospuaye.BackendOspuaye.Entity.Usuario;
import com.Ospuaye.BackendOspuaye.Service.DocumentoService;
import com.Ospuaye.BackendOspuaye.Service.PedidoOrtopediaService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    public ResponseEntity<List<PedidoOrtopedia>> listarPedidos() throws Exception {
        return ResponseEntity.ok(service.listar());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> crearPedido(
            @RequestPart("pedido") String pedidoJson,
            @RequestPart("usuario") String usuarioJson,
            @RequestPart("documentos") List<MultipartFile> files
    ) throws Exception {

        PedidoOrtopedia pedido = objectMapper.readValue(pedidoJson, PedidoOrtopedia.class);
        Usuario usuario = objectMapper.readValue(usuarioJson, Usuario.class);

        List<Documento> documentos = new ArrayList<>();
        for (MultipartFile file : files) {
            documentoService.handleFileUpload(file);

            Documento doc = Documento.builder()
                    .nombreArchivo(file.getOriginalFilename())
                    .path("C://Ospuaye/documentos/" + file.getOriginalFilename())
                    .observacion("Estudio previo adjunto")
                    .build();

            documentos.add(doc);
        }

        PedidoOrtopedia creado = service.crearPedido(pedido, documentos, usuario);
        return ResponseEntity.ok(creado);
    }
}
