package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Dto.PedidoOftalmologiaDTO;
import com.Ospuaye.BackendOspuaye.Dto.PedidoOrtopediaDTO;
import com.Ospuaye.BackendOspuaye.Entity.Documento;
import com.Ospuaye.BackendOspuaye.Entity.Enum.Estado;
import com.Ospuaye.BackendOspuaye.Entity.Pedido;
import com.Ospuaye.BackendOspuaye.Entity.PedidoOftalmologia;
import com.Ospuaye.BackendOspuaye.Entity.PedidoOrtopedia;
import com.Ospuaye.BackendOspuaye.Service.DocumentoService;
import com.Ospuaye.BackendOspuaye.Service.PedidoOrtopediaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
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
                    String nombreArchivo = documentoService.handleFileUpload(file);
                    // ✅ Guardar solo el nombre del archivo, no la ruta completa
                    Documento doc = Documento.builder()
                            .nombreArchivo(file.getOriginalFilename())
                            .path(nombreArchivo)  // Solo el nombre único generado
                            .observacion("Documento adjunto")
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

    @GetMapping("/buscar")
    public ResponseEntity<Page<Pedido>> buscar(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(pedidoOrtopediaService.buscar(query, page, size));
    }

    @GetMapping("/buscar-inactivos")
    public ResponseEntity<Page<Pedido>> buscarInactivos(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(pedidoOrtopediaService.buscarInactivos(query, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoOrtopediaDTO> obtenerPedidoPorId(@PathVariable Long id) {
        PedidoOrtopediaDTO dto = pedidoOrtopediaService.obtenerDto(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/libres")
    public ResponseEntity<?> TodosLosPedidosATomar() {
        try {
            List<PedidoOrtopedia> lista = pedidoOrtopediaService.listarPedidosOrtopediaSinMedico();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al cargar pedidos: " + e.getMessage());
        }
    }

}
