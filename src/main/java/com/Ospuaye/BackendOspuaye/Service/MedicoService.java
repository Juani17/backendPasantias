package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Dto.UpdateMedicoDTO;
import com.Ospuaye.BackendOspuaye.Entity.Medico;
import com.Ospuaye.BackendOspuaye.Entity.Usuario;
import com.Ospuaye.BackendOspuaye.Entity.Area;
import com.Ospuaye.BackendOspuaye.Repository.MedicoRepository;
import com.Ospuaye.BackendOspuaye.Repository.UsuarioRepository;
import com.Ospuaye.BackendOspuaye.Repository.AreaRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
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
    @Transactional(readOnly = true)
    public Page<Medico> buscar(String query, int page, int size) {
        if (page < 0) page = 0;
        if (size <= 0) size = 5;

        Pageable pageable = PageRequest.of(page, size);

        if (query == null || query.trim().isEmpty()) {
            return super.paginar(page, size);
        }

        String q = query.trim();

        // 🔍 Si es numérico → buscar por DNI
        if (q.matches("\\d+")) {
            try {
                Long dni = Long.parseLong(q);
                return medicoRepository.findByDni(dni, pageable);
            } catch (NumberFormatException ignored) {}
        }

        // 🔍 Si es texto → buscar por matrícula, nombre o apellido
        return medicoRepository.findByMatriculaContainingIgnoreCaseOrNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
                q, q, q, pageable
        );
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

    @Transactional
    public Medico update(Long id, UpdateMedicoDTO dto) {

        // Buscar médico
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        // Actualizar datos básicos
        medico.setNombre(dto.getNombre());
        medico.setApellido(dto.getApellido());
        medico.setDni(dto.getDni());
        medico.setCuil(dto.getCuil());
        medico.setTelefono(dto.getTelefono());
        medico.setSexo(dto.getSexo());
        medico.setEstado(dto.getEstado());
        medico.setMatricula(dto.getMatricula());

        // Actualizar área (si viene)
        if (dto.getAreaId() != null) {
            Area area = areaRepository.findById(dto.getAreaId())
                    .orElseThrow(() -> new RuntimeException("Área no encontrada"));
            medico.setArea(area);
        }

        // Actualizar usuario interno (email y/o contraseña)
        if (medico.getUsuario() != null) {
            Usuario usuario = medico.getUsuario();

            if (dto.getEmail() != null) {
                usuario.setEmail(dto.getEmail());
            }

            if (dto.getContrasena() != null && !dto.getContrasena().isBlank()) {
                usuario.setContrasena(dto.getContrasena());
            }

            usuarioRepository.save(usuario);
        }

        // Guardar médico
        return medicoRepository.save(medico);
    }


    @Transactional(readOnly = true)
    public ByteArrayResource exportarTXT() {

        List<Medico> medicos = medicoRepository.findAll();
        StringBuilder sb = new StringBuilder();

        sb.append("id|nombre|apellido|dni|cuil|telefono|matricula|area|correo\n");

        for (Medico m : medicos) {
            sb.append(m.getId()).append("|")
                    .append(m.getNombre() != null ? m.getNombre() : "").append("|")
                    .append(m.getApellido() != null ? m.getApellido() : "").append("|")
                    .append(m.getDni() != null ? m.getDni() : "").append("|")
                    .append(m.getCuil() != null ? m.getCuil() : "").append("|")
                    .append(m.getTelefono() != null ? m.getTelefono() : "").append("|")
                    .append(m.getMatricula() != null ? m.getMatricula() : "").append("|")
                    .append(m.getArea() != null ? m.getArea().getNombre() : "").append("|")
                    .append(m.getCorreoElectronico() != null ? m.getCorreoElectronico() : "")
                    .append("\n");
        }

        return new ByteArrayResource(sb.toString().getBytes());
    }

    // Utilidad para evitar nulls
    private String safe(Object o) {
        return o == null ? "" : o.toString();
    }


}
