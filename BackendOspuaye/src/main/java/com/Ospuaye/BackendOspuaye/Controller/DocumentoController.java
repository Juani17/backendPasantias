package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Documento;
import com.Ospuaye.BackendOspuaye.Service.DocumentoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoController extends BaseController<Documento, Long> {
    public DocumentoController(DocumentoService service) {
        super(service);
    }
}
