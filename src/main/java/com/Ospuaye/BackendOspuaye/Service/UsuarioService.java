package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Usuario;
import com.Ospuaye.BackendOspuaye.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UsuarioService extends BaseService<Usuario, Long> {

    private final UsuarioRepository usuarioRepository;
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public UsuarioService(UsuarioRepository usuarioRepository) {
        super(usuarioRepository);
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario crear(Usuario usuario) throws Exception {
        validarEmail(usuario.getEmail());
        if (emailExiste(usuario.getEmail())) {
            throw new Exception("El email ya está registrado");
        }
        if (usuario.getContrasena() == null || usuario.getContrasena().length() < 6) {
            throw new Exception("La contraseña debe tener al menos 6 caracteres");
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizar(Usuario usuario) throws Exception {
        validarEmail(usuario.getEmail());
        if (!usuarioRepository.existsById(usuario.getId())) {
            throw new Exception("No se encontró el usuario con el ID proporcionado");
        }
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public boolean emailExiste(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    private void validarEmail(String email) throws Exception {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new Exception("Formato de email inválido");
        }
    }
}
