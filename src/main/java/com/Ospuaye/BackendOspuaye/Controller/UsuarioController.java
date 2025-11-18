package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Dto.CambiarContrasenaRequest;
import com.Ospuaye.BackendOspuaye.Entity.Usuario;
import com.Ospuaye.BackendOspuaye.Service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController extends BaseController<Usuario, Long> {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        super(usuarioService);
        this.usuarioService = usuarioService;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario) {
        try {
            Usuario creado = usuarioService.crear(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {
        try {
            usuario.setId(id);
            Usuario actualizado = usuarioService.actualizar(usuario);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> buscarPorEmail(@PathVariable String email) {
        Optional<Usuario> opt = usuarioService.buscarPorEmail(email);
        if (opt.isPresent()) {
            return ResponseEntity.ok(opt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            List<Usuario> lista = usuarioService.listar();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Optional<Usuario> opt = usuarioService.buscarPorId(id);
            if (opt.isPresent()) {
                return ResponseEntity.ok(opt.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Usuario no encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            usuarioService.eliminar(id);
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/cambiarContrasena/{id}")
    public ResponseEntity<?> cambiarContrasena(
            @PathVariable Long id,
            @Valid @RequestBody CambiarContrasenaRequest request
    ) {
        try {
            // Llamamos al service pasando el id del usuario
            usuarioService.cambiarContrasena(
                    id,
                    request.getActual(),
                    request.getNueva(),
                    request.getNueva() // usamos la misma como confirmación
            );

            return ResponseEntity.ok("Contraseña cambiada correctamente");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al cambiar la contraseña");
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscar(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        try {
            return ResponseEntity.ok(usuarioService.buscar(query, page, size));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
