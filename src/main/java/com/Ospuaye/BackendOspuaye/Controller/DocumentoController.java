package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Documento;
import com.Ospuaye.BackendOspuaye.Service.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoController extends BaseController<Documento, Long> {

    @Autowired
    DocumentoService documentoService;

    public DocumentoController(DocumentoService service) {
        super(service);
        this.documentoService = service;
    }

    @PostMapping("/cargar")
    public ResponseEntity<String> uploadPic(@RequestParam("file") MultipartFile file) {
        try {
            return new ResponseEntity<>(documentoService.handleFileUpload(file), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al subir: " + e.getMessage(), HttpStatus.BAD_REQUEST);
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
}
