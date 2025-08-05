package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Area;
import com.Ospuaye.BackendOspuaye.Service.AreaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/areas")
public class AreaController extends BaseController<Area, Long> {
    public AreaController(AreaService service) {
        super(service);
    }
}
