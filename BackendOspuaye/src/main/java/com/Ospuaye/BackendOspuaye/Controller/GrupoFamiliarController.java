package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.GrupoFamiliar;
import com.Ospuaye.BackendOspuaye.Service.GrupoFamiliarService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/grupos-familiares")
public class GrupoFamiliarController extends BaseController<GrupoFamiliar, Long> {
    public GrupoFamiliarController(GrupoFamiliarService service) {
        super(service);
    }
}
