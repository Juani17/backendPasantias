package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Rol;
import com.Ospuaye.BackendOspuaye.Entity.Usuario;
import com.Ospuaye.BackendOspuaye.Repository.BaseRepository;
import com.Ospuaye.BackendOspuaye.Repository.RolRepository;
import com.Ospuaye.BackendOspuaye.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;



@Service
public class UsuarioService extends BaseService<Usuario, Long> {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public UsuarioService(BaseRepository<Usuario, Long> baseRepository, UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        super(baseRepository);
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    @Override
    public Usuario crear(Usuario usuario) throws Exception {
        validarEmail(usuario.getEmail());
        if (emailExiste(usuario.getEmail())) {
            throw new Exception("El email ya está registrado");
        }
        validarPassword(usuario.getContrasena());
        validarRolExiste(usuario.getRol());
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizar(Usuario usuario) throws Exception {
        if (usuario.getId() == null || !usuarioRepository.existsById(usuario.getId())) {
            throw new Exception("No se encontró el usuario con el ID proporcionado");
        }

        // Recuperamos el usuario actual de la BD
        Usuario usuarioExistente = usuarioRepository.findById(usuario.getId())
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        // Validar y actualizar email
        if (usuario.getEmail() != null) {
            validarEmail(usuario.getEmail());
            Optional<Usuario> existente = usuarioRepository.findByEmail(usuario.getEmail());
            if (existente.isPresent() && !existente.get().getId().equals(usuario.getId())) {
                throw new Exception("El nuevo email ya está en uso");
            }
            usuarioExistente.setEmail(usuario.getEmail());
        }

        // Validar y actualizar contraseña
        if (usuario.getContrasena() != null && !usuario.getContrasena().isBlank()) {
            validarPassword(usuario.getContrasena());
            usuarioExistente.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        }

        // Validar y actualizar rol
        if (usuario.getRol() != null) {
            validarRolExiste(usuario.getRol());
            usuarioExistente.setRol(usuario.getRol());
        }

        return usuarioRepository.save(usuarioExistente);
    }

    // Helpers
    public boolean emailExiste(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    private void validarEmail(String email) throws Exception {
        if (email == null || email.isBlank() || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new Exception("Formato de email inválido");
        }
    }
    private void validarPassword(String raw) throws Exception {
        if (raw == null || raw.length() < 6) {
            throw new Exception("La contraseña debe tener al menos 6 caracteres");
        }
    }
    private void validarRolExiste(Rol rol) throws Exception {
        if (rol == null || rol.getId() == null || !rolRepository.existsById(rol.getId())) {
            throw new Exception("El rol proporcionado no existe");
        }
    }
}
