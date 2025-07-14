package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.FormularioPodologia;
import com.Ospuaye.BackendOspuaye.Service.FormularioPodologiaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/formularios/podologia")
public class FormularioPodologiaController extends BaseController<FormularioPodologia, Long> {
    public FormularioPodologiaController(FormularioPodologiaService service) {
        super(service);
    }
}
