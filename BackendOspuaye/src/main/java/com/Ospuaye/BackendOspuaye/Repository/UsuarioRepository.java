package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends BaseRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}
