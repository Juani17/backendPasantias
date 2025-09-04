package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Usuario;
import com.Ospuaye.BackendOspuaye.Repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService extends BaseService<Usuario, Long> {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        super(usuarioRepository);
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Usuario crear(Usuario entity) throws Exception {
        if (entity == null) throw new IllegalArgumentException("El usuario no puede ser nulo");
        if (entity.getEmail() == null || entity.getEmail().isBlank())
            throw new IllegalArgumentException("El email es obligatorio");
        String email = entity.getEmail().trim().toLowerCase();
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))
            throw new IllegalArgumentException("El email no tiene un formato válido");
        if (usuarioRepository.existsByEmail(email))
            throw new IllegalArgumentException("El email ya está registrado");

        if (entity.getContrasena() == null || entity.getContrasena().isBlank())
            throw new IllegalArgumentException("La contraseña es obligatoria");
        if (entity.getContrasena().length() < 6)
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");

        entity.setEmail(email);
        entity.setContrasena(passwordEncoder.encode(entity.getContrasena()));

        // rol es opcional; si lo envías se guarda tal cual.
        return usuarioRepository.save(entity);
    }

    @Override
    @Transactional
    public Usuario actualizar(Usuario entity) throws Exception {
        if (entity == null || entity.getId() == null)
            throw new IllegalArgumentException("El usuario o su ID no pueden ser nulos");

        Usuario existente = usuarioRepository.findById(entity.getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // email
        if (entity.getEmail() != null && !entity.getEmail().isBlank()) {
            String nuevoEmail = entity.getEmail().trim().toLowerCase();
            if (!nuevoEmail.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))
                throw new IllegalArgumentException("El email no tiene un formato válido");
            if (!nuevoEmail.equalsIgnoreCase(existente.getEmail())
                    && usuarioRepository.existsByEmail(nuevoEmail)) {
                throw new IllegalArgumentException("El email ya está registrado por otro usuario");
            }
            existente.setEmail(nuevoEmail);
        }

        // NO cambiamos contraseña aquí para evitar riesgos.
        // Usar el método cambiarContrasena(...) de abajo.

        if (entity.getRol() != null) {
            existente.setRol(entity.getRol());
        }

        return usuarioRepository.save(existente);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        if (email == null || email.isBlank()) return Optional.empty();
        return usuarioRepository.findByEmail(email.trim().toLowerCase());
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarPorRol(String nombreRol) {
        if (nombreRol == null || nombreRol.isBlank())
            throw new IllegalArgumentException("El nombre del rol es obligatorio");
        return usuarioRepository.findByRol_NombreIgnoreCase(nombreRol.trim());
    }

    /** Cambio de contraseña seguro (valida contraseña actual y confirmación). */
    @Transactional
    public void cambiarContrasena(Long usuarioId,
                                  String contrasenaActual,
                                  String nuevaContrasena,
                                  String confirmarContrasena) throws Exception {
        if (usuarioId == null) throw new IllegalArgumentException("El ID de usuario es obligatorio");
        if (contrasenaActual == null || contrasenaActual.isBlank())
            throw new IllegalArgumentException("Debe indicar la contraseña actual");
        if (nuevaContrasena == null || nuevaContrasena.isBlank())
            throw new IllegalArgumentException("Debe indicar la nueva contraseña");
        if (nuevaContrasena.length() < 6)
            throw new IllegalArgumentException("La nueva contraseña debe tener al menos 6 caracteres");
        if (!nuevaContrasena.equals(confirmarContrasena))
            throw new IllegalArgumentException("La confirmación de contraseña no coincide");

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!passwordEncoder.matches(contrasenaActual, usuario.getContrasena()))
            throw new IllegalArgumentException("La contraseña actual es incorrecta");

        if (passwordEncoder.matches(nuevaContrasena, usuario.getContrasena()))
            throw new IllegalArgumentException("La nueva contraseña no puede ser igual a la actual");

        usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));
        usuarioRepository.save(usuario);
    }
}
