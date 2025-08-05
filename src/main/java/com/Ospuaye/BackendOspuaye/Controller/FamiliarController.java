package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Familiar;
import com.Ospuaye.BackendOspuaye.Service.FamiliarService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/familiares")
public class FamiliarController extends BaseController<Familiar, Long> {
    public FamiliarController(FamiliarService service) {
        super(service);
    }
}
