package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.HistorialMovimiento;
import com.Ospuaye.BackendOspuaye.Service.HistorialMovimientoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/historiales")
public class HistorialMovimientoController extends BaseController<HistorialMovimiento, Long> {
    public HistorialMovimientoController(HistorialMovimientoService service) {
        super(service);
    }
}
