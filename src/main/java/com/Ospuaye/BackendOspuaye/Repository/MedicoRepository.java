package com.Ospuaye.BackendOspuaye.Repository;

import com.Ospuaye.BackendOspuaye.Entity.Medico;
import com.Ospuaye.BackendOspuaye.Entity.Area;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends BaseRepository<Medico, Long> {
    Optional<Medico> findByUsuario_Id(Long usuarioId);
    List<Medico> findByArea(Area area);
}
