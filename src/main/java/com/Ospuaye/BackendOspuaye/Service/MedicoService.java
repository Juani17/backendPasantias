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

        // Usuario obligatorio
        if (entity.getUsuario() == null || entity.getUsuario().getId() == null)
            throw new IllegalArgumentException("El usuario es obligatorio");

        if (!usuarioRepository.existsById(entity.getUsuario().getId()))
            throw new IllegalArgumentException("Usuario no encontrado");

        if (medicoRepository.findByUsuario_Id(entity.getUsuario().getId()).isPresent())
            throw new IllegalArgumentException("Ya existe un médico asociado a este usuario");

        // Matrícula obligatoria
        if (entity.getMatricula() == null || entity.getMatricula().isEmpty())
            throw new IllegalArgumentException("La matrícula es obligatoria");

        // Área opcional
        if (entity.getArea() != null && entity.getArea().getId() != null) {
            Area a = areaRepository.findById(entity.getArea().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Área no encontrada"));
            entity.setArea(a);
        }

        return medicoRepository.save(entity);
    }

    @Override
    @Transactional
    public Medico actualizar(Medico entity) throws Exception {
        if (entity == null || entity.getId() == null)
            throw new IllegalArgumentException("La entidad o su ID no pueden ser nulos");

        Medico existente = medicoRepository.findById(entity.getId())
                .orElseThrow(() -> new IllegalArgumentException("Médico no encontrado"));

        // Validar usuario
        if (entity.getUsuario() != null && entity.getUsuario().getId() != null) {
            Long nuevoUsuarioId = entity.getUsuario().getId();
            if (!usuarioRepository.existsById(nuevoUsuarioId))
                throw new IllegalArgumentException("El usuario asociado no existe");

            Optional<Medico> byUser = medicoRepository.findByUsuario_Id(nuevoUsuarioId);
            if (byUser.isPresent() && !byUser.get().getId().equals(entity.getId()))
                throw new IllegalArgumentException("El usuario ya está vinculado a otro médico");

            existente.setUsuario(entity.getUsuario());
        }

        // Matrícula
        if (entity.getMatricula() != null && !entity.getMatricula().isEmpty())
            existente.setMatricula(entity.getMatricula());

        // Área (puede cambiar o quedar null)
        if (entity.getArea() != null && entity.getArea().getId() != null) {
            Area a = areaRepository.findById(entity.getArea().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Área no encontrada"));
            existente.setArea(a);
        } else {
            existente.setArea(null);
        }

        return medicoRepository.save(existente);
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
