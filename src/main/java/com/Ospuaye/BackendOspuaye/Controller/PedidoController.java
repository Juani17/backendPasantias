package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Documento;
import com.Ospuaye.BackendOspuaye.Entity.Enum.Estado;
import com.Ospuaye.BackendOspuaye.Entity.HistorialMovimiento;
import com.Ospuaye.BackendOspuaye.Entity.Pedido;
import com.Ospuaye.BackendOspuaye.Entity.PedidoOrtopedia;
import com.Ospuaye.BackendOspuaye.Service.DocumentoService;
import com.Ospuaye.BackendOspuaye.Service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController extends BaseController<Pedido, Long> {

    private final PedidoService pedidoService;
    private final DocumentoService documentoService;
    private final ObjectMapper objectMapper;

    // Constructor que pasa el service al BaseNombrableController
    public PedidoController(PedidoService pedidoService, DocumentoService documentoService, ObjectMapper objectMapper) {
        super(pedidoService); // 👈 importante
        this.pedidoService = pedidoService;
        this.documentoService = documentoService;
        this.objectMapper = objectMapper;
    }

    // =========================================================
    // ============= ENDPOINTS PERSONALIZADOS ==================
    // =========================================================

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> crearPedido(
            @RequestPart("pedido") String pedidoJson,
            @RequestPart(value = "documentos", required = false) List<MultipartFile> files
    ) {
        try {
            Pedido pedido = objectMapper.readValue(pedidoJson, Pedido.class);

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

            var creado = pedidoService.crearPedido(pedido, documentos);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear pedido: " + e.getMessage());
        }
    }

    @GetMapping("/todos")
    public ResponseEntity<?> TodosLosPedidos() {
        try {
            List<Pedido> lista = pedidoService.listarTodosLosPedidos();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al cargar pedidos: " + e.getMessage());
        }
    }

    @GetMapping("/todos/libres")
    public ResponseEntity<?> TodosLosPedidosATomar() {
        try {
            List<Pedido> lista = pedidoService.listarPedidosSinMedico();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al cargar pedidos: " + e.getMessage());
        }
    }

    @GetMapping("/genericos")
    public ResponseEntity<?> PedidosGenericos() {
        try {
            List<Pedido> lista = pedidoService.listarPedidosGenericos();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al cargar pedidos: " + e.getMessage());
        }
    }

    @GetMapping("/beneficiario/{id}")
    public ResponseEntity<?> listarPorBeneficiario(@PathVariable Long id) {
        try {
            List<Pedido> pedidos = pedidoService.findByBeneficiarioId(id);
            return ResponseEntity.ok(pedidos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener pedidos: " + e.getMessage());
        }
    }

    @GetMapping("/medico/{id}")
    public ResponseEntity<?> listarPorMedico(@PathVariable Long id) {
        try {
            List<Pedido> pedidos = pedidoService.findByMedicoId(id);
            return ResponseEntity.ok(pedidos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener pedidos: " + e.getMessage());
        }
    }

    @PutMapping(value = "/editar/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> actualizarPedido(
            @PathVariable Long id,
            @RequestPart("pedido") String pedidoJson,
            @RequestPart(value = "documentos", required = false) List<MultipartFile> files
    ) {
        try {
            Pedido pedidoActualizado = objectMapper.readValue(pedidoJson, Pedido.class);

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

            Pedido actualizado = pedidoService.actualizarPedidoGeneral(id, pedidoActualizado, documentos);
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

            Pedido actualizado = pedidoService.actualizarEstadoGeneral(id, nuevoEstado);
            return ResponseEntity.ok(actualizado);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar el estado: " + e.getMessage());
        }
    }

    @PutMapping("/tomar/{id}")
    public ResponseEntity<?> tomarPedido(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body
    ) {
        try {
            Long medicoId = null;

            if (body.get("medico_id") != null) {
                medicoId = Long.valueOf(body.get("medico_id").toString());
            } else if (body.get("medico") instanceof Map<?,?> medMap && medMap.get("id") != null) {
                medicoId = Long.valueOf(medMap.get("id").toString());
            }

            if (medicoId == null) {
                return ResponseEntity.badRequest().body("Debe indicar el ID del médico.");
            }

            Pedido actualizado = pedidoService.tomarPedidoGlobal(id, medicoId);
            return ResponseEntity.ok(actualizado);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al tomar pedido: " + e.getMessage());
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<Pedido>> buscar(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(pedidoService.buscar(query, page, size));
    }

    @GetMapping("/buscar-inactivos")
    public ResponseEntity<Page<Pedido>> buscarInactivos(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(pedidoService.buscarInactivos(query, page, size));
    }


}
