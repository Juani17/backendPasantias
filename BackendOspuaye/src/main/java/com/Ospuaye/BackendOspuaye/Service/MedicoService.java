package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Medico;
import com.Ospuaye.BackendOspuaye.Repository.MedicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicoService extends BaseService<Medico, Long> {

    private final MedicoRepository medicoRepository;

    public MedicoService(MedicoRepository repository) {
        super(repository);
        this.medicoRepository = repository;
    }

    @Override
    @Transactional
    public Medico crear(Medico medico) throws Exception {
        if (medico == null) throw new Exception("Medico no puede ser nulo");
        if (medico.getMatricula() == null || medico.getMatricula().trim().isEmpty())
            throw new Exception("Matrícula es obligatoria");
        if (medico.getArea() == null || medico.getArea().getId() == null)
            throw new Exception("Área es obligatoria para el médico");
        if (medico.getUsuario() == null || medico.getUsuario().getId() == null)
            throw new Exception("Usuario asociado es obligatorio");
        return medicoRepository.save(medico);
    }

    @Override
    @Transactional
    public Medico actualizar(Medico medico) throws Exception {
        if (medico == null || medico.getId() == null) throw new Exception("ID es obligatorio para actualizar Médico");
        if (!medicoRepository.existsById(medico.getId())) throw new Exception("Medico no encontrado");
        return crear(medico); // validaciones ya en crear
    }
}
