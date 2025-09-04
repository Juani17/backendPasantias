package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Medico;
import com.Ospuaye.BackendOspuaye.Entity.Usuario;
import com.Ospuaye.BackendOspuaye.Entity.Area;
import com.Ospuaye.BackendOspuaye.Repository.MedicoRepository;
import com.Ospuaye.BackendOspuaye.Repository.UsuarioRepository;
import com.Ospuaye.BackendOspuaye.Repository.AreaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MedicoService extends BaseService<Medico, Long> {

    private final MedicoRepository medicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AreaRepository areaRepository;

    public MedicoService(MedicoRepository medicoRepository,
                         UsuarioRepository usuarioRepository,
                         AreaRepository areaRepository) {
        super(medicoRepository);
        this.medicoRepository = medicoRepository;
        this.usuarioRepository = usuarioRepository;
        this.areaRepository = areaRepository;
    }

    @Override
    @Transactional
    public Medico crear(Medico entity) throws Exception {
        if (entity == null) throw new IllegalArgumentException("El médico no puede ser nulo");

        if (entity.getUsuario() == null || entity.getUsuario().getId() == null)
            throw new IllegalArgumentException("El usuario es obligatorio");

        if (!usuarioRepository.existsById(entity.getUsuario().getId()))
            throw new IllegalArgumentException("Usuario no encontrado");

        if (medicoRepository.findByUsuario_Id(entity.getUsuario().getId()).isPresent())
            throw new IllegalArgumentException("Ya existe un médico asociado a este usuario");

        if (entity.getPersona() == null || entity.getPersona().getId() == null)
            throw new IllegalArgumentException("La persona es obligatoria");

        if (entity.getMatricula() == null || entity.getMatricula().isEmpty())
            throw new IllegalArgumentException("La matrícula es obligatoria");

        if (entity.getArea() != null && entity.getArea().getId() != null) {
            Area a = areaRepository.findById(entity.getArea().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Área no encontrada"));
            entity.setArea(a);
        }

        return medicoRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public Optional<Medico> buscarPorUsuarioId(Long usuarioId) {
        if (usuarioId == null) return Optional.empty();
        return medicoRepository.findByUsuario_Id(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<Medico> listarPorArea(Long areaId) throws Exception {
        if (areaId == null) throw new IllegalArgumentException("ID de área no puede ser nulo");
        Area a = areaRepository.findById(areaId)
                .orElseThrow(() -> new IllegalArgumentException("Área no encontrada"));
        return medicoRepository.findByArea(a);
    }
}
