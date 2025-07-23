package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Entity.Usuario;
import com.Ospuaye.BackendOspuaye.Service.BaseService;
import com.Ospuaye.BackendOspuaye.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController extends BaseController<Usuario, Long> {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        super(usuarioService);
        this.usuarioService = usuarioService;
    }

    // Método adicional que no está en BaseController
    @GetMapping("/buscarPorEmail")
    public ResponseEntity<?> buscarPorEmail(@RequestParam String email) {
        Optional<Usuario> usuario = usuarioService.buscarPorEmail(email);
        return usuario.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Método personalizado para creación con validación de email
    @PostMapping("/crear")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        if (usuarioService.emailExiste(usuario.getEmail())) {
            return ResponseEntity.badRequest().body("El email ya está en uso");
        }
        return ResponseEntity.ok(usuarioService.crear(usuario));
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizarUsuario(@RequestBody Usuario usuario) {
        Optional<Usuario> existente = usuarioService.buscarPorId(usuario.getId());
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (!existente.get().getEmail().equals(usuario.getEmail()) &&
                usuarioService.emailExiste(usuario.getEmail())) {
            return ResponseEntity.badRequest().body("El nuevo email ya está en uso");
        }

        return ResponseEntity.ok(usuarioService.actualizar(usuario));
    }
}
