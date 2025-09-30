package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Documento;
import com.Ospuaye.BackendOspuaye.Entity.Pedido;
import com.Ospuaye.BackendOspuaye.Service.DocumentoService;
import com.Ospuaye.BackendOspuaye.Service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class PedidoController<E extends Pedido, S extends PedidoService<E>> extends BaseController<E, Long> {

    protected final S pedidoService;
    protected final DocumentoService documentoService;
    protected final ObjectMapper objectMapper;

    public PedidoController(S service, DocumentoService documentoService, ObjectMapper objectMapper) {
        super(service);
        this.pedidoService = service;
        this.documentoService = documentoService;
        this.objectMapper = objectMapper;
    }

    // Métodos abstractos específicos
    protected abstract E crearPedidoEspecifico(E pedido, List<Documento> documentos) throws Exception;
    protected abstract Class<E> getPedidoClass();
    protected abstract String getObservacionDocumento();

    // LISTAR TODOS LOS PEDIDOS
    @Override
    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            List<E> pedidos = pedidoService.listar();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", pedidos,
                    "total", pedidos.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Error al obtener pedidos",
                            "error", e.getMessage()
                    ));
        }
    }

    // BUSCAR POR ID
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "ID es obligatorio"));
            }

            Optional<E> pedidoOpt = pedidoService.buscarPorId(id);
            if (pedidoOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "Pedido no encontrado"));
            }

            return ResponseEntity.ok(Map.of("success", true, "data", pedidoOpt.get()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error al obtener pedido", "error", e.getMessage()));
        }
    }

    // ELIMINAR PEDIDO
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "ID es obligatorio"));
            }

            pedidoService.eliminar(id);
            return ResponseEntity.ok(Map.of("success", true, "message", "Pedido eliminado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Error al eliminar pedido", "error", e.getMessage()));
        }
    }

    // CREAR PEDIDO CON DOCUMENTOS
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> crearPedidoConDocumentos(
            @RequestPart("pedido") String pedidoJson,
            @RequestPart(value = "documentos", required = false) List<MultipartFile> files
    ) {
        try {
            if (pedidoJson == null || pedidoJson.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Datos del pedido son obligatorios"));
            }

            // Deserializar pedido
            E pedido = objectMapper.readValue(pedidoJson, getPedidoClass());

            // Procesar documentos
            List<Documento> documentos = procesarDocumentos(files);

            // Guardar usando el método específico
            E creado = crearPedidoEspecifico(pedido, documentos);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("success", true, "message", "Pedido creado exitosamente", "data", creado));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Error al crear pedido", "error", e.getMessage()));
        }
    }

    // BÚSQUEDAS ESPECÍFICAS
    @GetMapping("/beneficiario/{beneficiarioId}")
    public ResponseEntity<?> buscarPorBeneficiario(@PathVariable Long beneficiarioId) {
        try {
            List<E> pedidos = pedidoService.buscarPorBeneficiario(beneficiarioId);
            return ResponseEntity.ok(Map.of("success", true, "data", pedidos, "total", pedidos.size()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Error al buscar por beneficiario", "error", e.getMessage()));
        }
    }

    @GetMapping("/grupo-familiar/{grupoId}")
    public ResponseEntity<?> buscarPorGrupoFamiliar(@PathVariable Long grupoId) {
        try {
            List<E> pedidos = pedidoService.buscarPorGrupoFamiliar(grupoId);
            return ResponseEntity.ok(Map.of("success", true, "data", pedidos, "total", pedidos.size()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Error al buscar por grupo familiar", "error", e.getMessage()));
        }
    }

    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<?> buscarPorMedico(@PathVariable Long medicoId) {
        try {
            List<E> pedidos = pedidoService.buscarPorMedico(medicoId);
            return ResponseEntity.ok(Map.of("success", true, "data", pedidos, "total", pedidos.size()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Error al buscar por médico", "error", e.getMessage()));
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> buscarPorUsuario(@PathVariable Long usuarioId) {
        try {
            List<E> pedidos = pedidoService.buscarPorUsuario(usuarioId);
            return ResponseEntity.ok(Map.of("success", true, "data", pedidos, "total", pedidos.size()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Error al buscar por usuario", "error", e.getMessage()));
        }
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<?> buscarPorPaciente(@PathVariable Long pacienteId) {
        try {
            List<E> pedidos = pedidoService.buscarPorPaciente(pacienteId);
            return ResponseEntity.ok(Map.of("success", true, "data", pedidos, "total", pedidos.size()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Error al buscar por paciente", "error", e.getMessage()));
        }
    }

    // MÉTODO PRIVADO PARA PROCESAR DOCUMENTOS
    protected List<Documento> procesarDocumentos(List<MultipartFile> files) throws Exception {
        List<Documento> documentos = new ArrayList<>();

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (file.isEmpty()) throw new Exception("No se pueden subir archivos vacíos");

                // Subir archivo físicamente
                String nombreArchivoFisico = documentoService.subirArchivo(file);

                // Crear entidad Documento
                Documento doc = Documento.builder()
                        .nombreArchivo(file.getOriginalFilename())
                        .path("C://Ospuaye/documentos/" + nombreArchivoFisico)
                        .observacion(getObservacionDocumento())
                        .build();

                documentos.add(doc);
            }
        }

        return documentos;
    }
}
