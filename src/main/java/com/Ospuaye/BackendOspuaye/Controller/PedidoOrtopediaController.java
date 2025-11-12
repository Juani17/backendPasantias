package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Documento;
import com.Ospuaye.BackendOspuaye.Entity.Enum.Estado;
import com.Ospuaye.BackendOspuaye.Entity.PedidoOftalmologia;
import com.Ospuaye.BackendOspuaye.Entity.PedidoOrtopedia;
import com.Ospuaye.BackendOspuaye.Service.DocumentoService;
import com.Ospuaye.BackendOspuaye.Service.PedidoOrtopediaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos/ortopedia")
@RequiredArgsConstructor
public class PedidoOrtopediaController {

    private final PedidoOrtopediaService pedidoOrtopediaService;
    private final DocumentoService documentoService;
    private final ObjectMapper objectMapper;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> crearPedido(
            @RequestPart("pedido") String pedidoJson,
            @RequestPart(value = "documentos", required = false) List<MultipartFile> files
    ) {
        try {
            PedidoOrtopedia pedido = objectMapper.readValue(pedidoJson, PedidoOrtopedia.class);

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
                            .observacion("Estudio de ortopedia adjunto")
                            .build();
                    documentos.add(doc);
                }
            }

            // ✅ CORRECTO - llamar al método específico
            var creado = pedidoOrtopediaService.crearPedidoOrtopedia(pedido, documentos);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear el pedido: " + e.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarTodos() {
        try {
            return ResponseEntity.ok(pedidoOrtopediaService.listarTodos());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al listar pedidos: " + e.getMessage());
        }
    }

    @GetMapping("/beneficiario/{id}")
    public ResponseEntity<?> listarPorBeneficiario(@PathVariable Long id) {
        try {
            List<PedidoOrtopedia> pedidos = pedidoOrtopediaService.listarPorBeneficiario(id);
            return ResponseEntity.ok(pedidos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener pedidos: " + e.getMessage());
        }
    }

    @GetMapping("/medico/{id}")
    public ResponseEntity<?> listarPorMedico(@PathVariable Long id) {
        try {
            List<PedidoOrtopedia> pedidos = pedidoOrtopediaService.listarPorMedico(id);
            return ResponseEntity.ok(pedidos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener pedidos: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPedido(
            @PathVariable Long id,
            @RequestBody PedidoOrtopedia pedidoActualizado
    ) {
        try {
            PedidoOrtopedia actualizado = pedidoOrtopediaService.actualizarPedidoOrtopedia(id, pedidoActualizado);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar el pedido: " + e.getMessage());
        }
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        try {
            String estadoStr = body.get("estado");
            if (estadoStr == null || estadoStr.isBlank()) {
                return ResponseEntity.badRequest().body("Debe enviar un estado válido");
            }

            Estado nuevoEstado;
            try {
                nuevoEstado = Estado.valueOf(estadoStr);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("El estado '" + estadoStr + "' no es válido. " +
                        "Debe ser uno de: Pendiente, Aceptado, Rechazado, Leido");
            }

            PedidoOrtopedia actualizado = pedidoOrtopediaService.actualizarEstadoOrtopedia(id, nuevoEstado);
            return ResponseEntity.ok(actualizado);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar el estado: " + e.getMessage());
        }
    }
}
