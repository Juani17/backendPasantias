package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Rol;
import com.Ospuaye.BackendOspuaye.Service.RolService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
public class RolController extends BaseController<Rol, Long> {
    public RolController(RolService service) {
        super(service);
    }
}