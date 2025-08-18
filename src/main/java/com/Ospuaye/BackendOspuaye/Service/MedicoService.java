package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Area;
import com.Ospuaye.BackendOspuaye.Entity.Medico;
import com.Ospuaye.BackendOspuaye.Entity.Usuario;
import com.Ospuaye.BackendOspuaye.Repository.AreaRepository;
import com.Ospuaye.BackendOspuaye.Repository.MedicoRepository;
import com.Ospuaye.BackendOspuaye.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class MedicoService extends BaseService<Medico, Long> {

    private final MedicoRepository medicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AreaRepository areaRepository;

    public MedicoService(MedicoRepository repository,
                         UsuarioRepository usuarioRepository,
                         AreaRepository areaRepository) {
        super(repository);
        this.medicoRepository = repository;
        this.usuarioRepository = usuarioRepository;
        this.areaRepository = areaRepository;
    }

    @Override
    public Medico crear(Medico m) throws Exception {
        validar(m, null);
        return medicoRepository.save(m);
    }

    @Override
    public Medico actualizar(Medico m) throws Exception {
        if (m.getId() == null || !medicoRepository.existsById(m.getId())) {
            throw new Exception("Médico no encontrado");
        }
        validar(m, m.getId());
        return medicoRepository.save(m);
    }

    private void validar(Medico m, Long idActual) throws Exception {
        if (m.getNombre() == null || m.getNombre().isBlank()) throw new Exception("El nombre es obligatorio");
        if (m.getApellido() == null || m.getApellido().isBlank()) throw new Exception("El apellido es obligatorio");
        if (m.getMatricula() == null || m.getMatricula().isBlank()) throw new Exception("La matrícula es obligatoria");

        var existMat = medicoRepository.findByMatricula(m.getMatricula());
        if (existMat.isPresent() && (idActual == null || !existMat.get().getId().equals(idActual))) {
            throw new Exception("La matrícula ya está registrada");
        }

        Usuario u = m.getUsuario();
        if (u == null || u.getId() == null || !usuarioRepository.existsById(u.getId())) {
            throw new Exception("El usuario asociado no existe");
        }
        // que un usuario no se repita en 2 médicos
        var existUser = medicoRepository.findByUsuarioId(u.getId());
        if (existUser.isPresent() && (idActual == null || !existUser.get().getId().equals(idActual))) {
            throw new Exception("El usuario ya está vinculado a otro médico");
        }

        Area a = m.getArea();
        if (a == null || a.getId() == null || !areaRepository.existsById(a.getId())) {
            throw new Exception("El área asociada no existe");
        }
    }
}
