package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Dto.DocumentoDTO;
import com.Ospuaye.BackendOspuaye.Entity.Documento;
import com.Ospuaye.BackendOspuaye.Service.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoController extends BaseController<Documento, Long> {

    @Autowired
    DocumentoService documentoService;

    @Value("${app.documentos.url-base:/api/documentos/descargar}")
    private String urlBase;

    public DocumentoController(DocumentoService service) {
        super(service);
        this.documentoService = service;
    }

    @PostMapping("/cargar")
    public ResponseEntity<String> uploadPic(@RequestParam("file") MultipartFile file) {
        try {
            String nombreArchivo = documentoService.handleFileUpload(file);
            return ResponseEntity.ok(nombreArchivo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al subir: " + e.getMessage());
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@Valid @RequestBody Documento documento) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(documentoService.crear(documento));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizar(@Valid @RequestBody Documento documento) {
        try {
            return ResponseEntity.ok(documentoService.actualizar(documento));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * ✅ Endpoint para descargar/ver archivos
     */
    @GetMapping("/descargar/{nombreArchivo}")
    public ResponseEntity<Resource> descargarArchivo(@PathVariable String nombreArchivo) {
        try {
            Path archivoPath = documentoService.obtenerRutaArchivo(nombreArchivo);

            if (!documentoService.existeArchivo(nombreArchivo)) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(archivoPath.toUri());

            // Determinar el tipo de contenido
            String contentType = determinarContentType(nombreArchivo);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + nombreArchivo + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * ✅ Obtener lista de documentos con URLs completas por pedido
     */
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<?> obtenerDocumentosPorPedido(@PathVariable Long pedidoId) {
        try {
            List<Documento> documentos = documentoService.obtenerDocumentosPorPedido(pedidoId);

            if (documentos.isEmpty()) {
                return ResponseEntity.ok(List.of()); // ✅ Retorna lista vacía en lugar de error
            }

            // Convertir a DTO con URLs completas
            List<DocumentoDTO> dtos = documentos.stream()
                    .map(doc -> {
                        DocumentoDTO dto = new DocumentoDTO();
                        dto.setId(doc.getId());
                        dto.setNombreArchivo(doc.getNombreArchivo());
                        dto.setObservacion(doc.getObservacion());
                        dto.setFechaSubida(doc.getFechaSubida());
                        dto.setSubidoPor(doc.getSubidoPor() != null ? doc.getSubidoPor().getEmail() : null);

                        // ✅ Extraer solo el nombre del archivo del path
                        String nombreArchivo = doc.getPath() != null && !doc.getPath().isEmpty()
                                ? doc.getPath().substring(doc.getPath().lastIndexOf("/") + 1)
                                : doc.getNombreArchivo();

                        dto.setUrl(urlBase + "/" + nombreArchivo);
                        dto.setPath(nombreArchivo); // ✅ Solo el nombre del archivo

                        return dto;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(dtos);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * ✅ Determinar el tipo MIME según la extensión
     */
    private String determinarContentType(String nombreArchivo) {
        String lower = nombreArchivo.toLowerCase();
        if (lower.endsWith(".pdf")) return "application/pdf";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".doc")) return "application/msword";
        if (lower.endsWith(".docx"))
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        return "application/octet-stream";
    }
}