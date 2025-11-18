package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import com.Ospuaye.BackendOspuaye.Service.BeneficiarioService;
import com.Ospuaye.BackendOspuaye.Service.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/beneficiarios")
public class BeneficiarioController extends BaseController<Beneficiario, Long> {

    private final BeneficiarioService beneficiarioService;
    private final EmpresaService empresaService;

    public BeneficiarioController(BeneficiarioService beneficiarioService, EmpresaService empresaService) {
        super(beneficiarioService);
        this.beneficiarioService = beneficiarioService;
        this.empresaService = empresaService;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@Valid @RequestBody Beneficiario beneficiario) {
        try {
            Beneficiario creado = beneficiarioService.crear(beneficiario);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody Beneficiario beneficiario) {
        try {
            beneficiario.setId(id);
            Beneficiario actualizado = beneficiarioService.actualizar(beneficiario);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            beneficiarioService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<?> buscarPorDni(@PathVariable Long dni) {
        try {
            Optional<Beneficiario> opt = beneficiarioService.buscarPorDni(dni);

            if (opt.isPresent()) {
                return ResponseEntity.ok(opt.get()); // Devuelve el Beneficiario encontrado
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Beneficiario no encontrado"); // Devuelve mensaje si no hay
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Devuelve mensaje de error
        }
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<?> listarPorEmpresa(@PathVariable Long empresaId) {
        try {
            List<?> lista = beneficiarioService.listarPorEmpresaId(empresaId);
            return ResponseEntity.ok(lista);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/afiliados-sindicato")
    public ResponseEntity<?> listarAfiliados() {
        try {
            List<?> lista = beneficiarioService.listarAfiliadosSindicato();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Assign empresa to beneficiario
    @PostMapping("/{beneficiarioId}/empresa/{empresaId}")
    public ResponseEntity<?> asignarEmpresa(@PathVariable Long beneficiarioId, @PathVariable Long empresaId) {
        try {
            Beneficiario b = beneficiarioService.buscarPorId(beneficiarioId)
                    .orElseThrow(() -> new IllegalArgumentException("Beneficiario no encontrado"));
            var empresa = empresaService.buscarPorId(empresaId)
                    .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));
            b.setEmpresa(empresa);
            Beneficiario actualizado = beneficiarioService.actualizar(b);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Remove company association
    @DeleteMapping("/{beneficiarioId}/empresa")
    public ResponseEntity<?> quitarEmpresa(@PathVariable Long beneficiarioId) {
        try {
            Beneficiario b = beneficiarioService.buscarPorId(beneficiarioId)
                    .orElseThrow(() -> new IllegalArgumentException("Beneficiario no encontrado"));
            b.setEmpresa(null);
            Beneficiario actualizado = beneficiarioService.actualizar(b);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // -------------------------------------------------------
    // BUSCAR + PAGINACIÓN
    // -------------------------------------------------------
    @GetMapping("/buscar")
    public ResponseEntity<Page<Beneficiario>> buscar(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        try {
            return ResponseEntity.ok(beneficiarioService.buscar(query, page, size));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
