package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Medico;
import com.Ospuaye.BackendOspuaye.Service.MedicoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicos")
public class MedicoController extends BaseController<Medico, Long> {
    public MedicoController(MedicoService service) {
        super(service);
    }
}
