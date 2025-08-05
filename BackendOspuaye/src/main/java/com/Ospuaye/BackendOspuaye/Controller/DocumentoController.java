package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Documento;
import com.Ospuaye.BackendOspuaye.Entity.PedidoOftalmologia;
import com.Ospuaye.BackendOspuaye.Service.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoController extends BaseController<Documento, Long> {
    public DocumentoController(DocumentoService service) {
        super(service);
    }

    @Autowired
    DocumentoService documentoService;


    @PostMapping("/cargar")
    public ResponseEntity<String> uploadPic(@RequestParam("file") MultipartFile file) throws Exception {
        return new ResponseEntity<>(documentoService.handleFileUpload(file), HttpStatus.OK);
    }
}
