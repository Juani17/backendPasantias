package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import com.Ospuaye.BackendOspuaye.Service.BeneficiarioService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/beneficiarios")
public class BeneficiarioController extends BaseController<Beneficiario, Long> {
    public BeneficiarioController(BeneficiarioService service) {
        super(service);
    }
}
