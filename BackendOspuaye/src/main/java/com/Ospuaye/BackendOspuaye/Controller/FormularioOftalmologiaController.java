package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.FormularioOftalmologia;
import com.Ospuaye.BackendOspuaye.Service.FormularioOftalmologiaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/formularios/oftalmologia")
public class FormularioOftalmologiaController extends BaseController<FormularioOftalmologia, Long> {
    public FormularioOftalmologiaController(FormularioOftalmologiaService service) {
        super(service);
    }
}
