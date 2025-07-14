package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.InterfaceRol;
import com.Ospuaye.BackendOspuaye.Service.InterfaceRolService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
public class InterfaceRolController extends BaseController<InterfaceRol, Long> {
    public InterfaceRolController(InterfaceRolService service) {
        super(service);
    }
}
