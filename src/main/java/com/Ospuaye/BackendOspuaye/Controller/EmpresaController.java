package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import com.Ospuaye.BackendOspuaye.Entity.Empresa;
import com.Ospuaye.BackendOspuaye.Service.BeneficiarioService;
import com.Ospuaye.BackendOspuaye.Service.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController extends BaseController<Empresa, Long> {

    private final EmpresaService empresaService;
    private final BeneficiarioService beneficiarioService;

    public EmpresaController(EmpresaService empresaService, BeneficiarioService beneficiarioService) {
        super(empresaService);
        this.empresaService = empresaService;
        this.beneficiarioService = beneficiarioService;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@Valid @RequestBody Empresa empresa) {
        try {
            Empresa creado = empresaService.crear(empresa);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody Empresa empresa) {
        try {
            empresa.setId(id);
            Empresa actualizado = empresaService.actualizar(empresa);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            empresaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/cuit/{cuit}")
    public ResponseEntity<?> buscarPorCuit(@PathVariable String cuit) {
        try {
            Empresa e = empresaService.buscarPorCuit(cuit);
            if (e == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa no encontrada");
            return ResponseEntity.ok(e);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/activo/{activo}")
    public ResponseEntity<?> listarPorActivo(@PathVariable Boolean activo) {
        try {
            return ResponseEntity.ok(empresaService.listarPorActivo(activo));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/{id}/beneficiarios")
    public ResponseEntity<?> listarBeneficiarios(@PathVariable Long id) {
        try {
            Empresa e = empresaService.buscarPorId(id).orElse(null);
            if (e == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa no encontrada");
            List<Beneficiario> lista = beneficiarioService.listarPorEmpresaId(id);
            return ResponseEntity.ok(lista);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}
