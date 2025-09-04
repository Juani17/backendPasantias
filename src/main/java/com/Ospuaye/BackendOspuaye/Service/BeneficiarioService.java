package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import com.Ospuaye.BackendOspuaye.Entity.Empresa;
import com.Ospuaye.BackendOspuaye.Repository.BeneficiarioRepository;
import com.Ospuaye.BackendOspuaye.Repository.EmpresaRepository;
import com.Ospuaye.BackendOspuaye.Repository.PersonaRepository;
import com.Ospuaye.BackendOspuaye.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BeneficiarioService extends BaseService<Beneficiario, Long> {

    private final BeneficiarioRepository beneficiarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final EmpresaRepository empresaRepository;

    public BeneficiarioService(BeneficiarioRepository beneficiarioRepository,
                               UsuarioRepository usuarioRepository,
                               PersonaRepository personaRepository,
                               EmpresaRepository empresaRepository) {
        super(beneficiarioRepository);
        this.beneficiarioRepository = beneficiarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.personaRepository = personaRepository;
        this.empresaRepository = empresaRepository;
    }

    @Override
    @Transactional
    public Beneficiario crear(Beneficiario entity) throws Exception {
        // Null checks
        if (entity == null) throw new IllegalArgumentException("El beneficiario no puede ser nulo");

        // Usuario validation
        if (entity.getUsuario() == null || entity.getUsuario().getId() == null)
            throw new IllegalArgumentException("El usuario asociado es obligatorio");
        if (!usuarioRepository.existsById(entity.getUsuario().getId()))
            throw new IllegalArgumentException("El usuario asociado no existe");
        if (beneficiarioRepository.existsByUsuario_Id(entity.getUsuario().getId()))
            throw new IllegalArgumentException("Ya existe un beneficiario asociado a ese usuario");

        // Persona validation
        if (entity.getPersona() == null || entity.getPersona().getId() == null)
            throw new IllegalArgumentException("La persona asociada es obligatoria");
        if (!personaRepository.existsById(entity.getPersona().getId()))
            throw new IllegalArgumentException("La persona asociada no existe");
        if (beneficiarioRepository.existsByPersona_Id(entity.getPersona().getId()))
            throw new IllegalArgumentException("Ya existe un beneficiario asociado a esa persona");

        // Empresa (opcional) validation
        if (entity.getEmpresa() != null) {
            Long empresaId = entity.getEmpresa().getId();
            if (empresaId == null) throw new IllegalArgumentException("La empresa debe tener id");
            Optional<Empresa> opt = empresaRepository.findById(empresaId);
            if (opt.isEmpty()) throw new IllegalArgumentException("La empresa asociada no existe");
            Empresa empresa = opt.get();
            if (empresa.getActivo() != null && !empresa.getActivo())
                throw new IllegalArgumentException("No se puede asociar a una empresa inactiva");
        }

        // Defaults
        if (entity.getAfiliadoSindical() == null) entity.setAfiliadoSindical(false);
        if (entity.getEsJubilado() == null) entity.setEsJubilado(false);

        return beneficiarioRepository.save(entity);
    }

    @Override
    @Transactional
    public Beneficiario actualizar(Beneficiario entity) throws Exception {
        if (entity == null || entity.getId() == null)
            throw new IllegalArgumentException("La entidad o su ID no pueden ser nulos");
        if (!beneficiarioRepository.existsById(entity.getId()))
            throw new IllegalArgumentException("Beneficiario no encontrado");

        Beneficiario existente = beneficiarioRepository.findById(entity.getId()).orElseThrow(() -> new IllegalArgumentException("Beneficiario no encontrado"));

        // If usuario changed, validate
        if (entity.getUsuario() != null && entity.getUsuario().getId() != null) {
            Long nuevoUsuarioId = entity.getUsuario().getId();
            if (!usuarioRepository.existsById(nuevoUsuarioId))
                throw new IllegalArgumentException("El usuario asociado no existe");
            Optional<Beneficiario> byUser = beneficiarioRepository.findByUsuario_Id(nuevoUsuarioId);
            if (byUser.isPresent() && !byUser.get().getId().equals(entity.getId()))
                throw new IllegalArgumentException("El usuario ya está vinculado a otro beneficiario");
            existente.setUsuario(entity.getUsuario());
        }

        // If persona changed, validate
        if (entity.getPersona() != null && entity.getPersona().getId() != null) {
            Long nuevaPersonaId = entity.getPersona().getId();
            if (!personaRepository.existsById(nuevaPersonaId))
                throw new IllegalArgumentException("La persona asociada no existe");
            Optional<Beneficiario> byPersona = beneficiarioRepository.findByPersona_Id(nuevaPersonaId);
            if (byPersona.isPresent() && !byPersona.get().getId().equals(entity.getId()))
                throw new IllegalArgumentException("La persona ya está vinculada a otro beneficiario");
            existente.setPersona(entity.getPersona());
        }

        // Empresa update (can set or remove)
        if (entity.getEmpresa() != null) {
            Long empresaId = entity.getEmpresa().getId();
            if (empresaId == null) throw new IllegalArgumentException("La empresa debe tener id");
            Empresa empresa = empresaRepository.findById(empresaId).orElseThrow(() -> new IllegalArgumentException("La empresa asociada no existe"));
            if (empresa.getActivo() != null && !empresa.getActivo())
                throw new IllegalArgumentException("No se puede asociar a una empresa inactiva");
            existente.setEmpresa(empresa);
        } else {
            // if explicitly null in payload, remove association
            existente.setEmpresa(null);
        }

        // Afiliado / jubilado flags
        if (entity.getAfiliadoSindical() != null) existente.setAfiliadoSindical(entity.getAfiliadoSindical());
        if (entity.getEsJubilado() != null) existente.setEsJubilado(entity.getEsJubilado());

        return beneficiarioRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Long id) throws Exception {
        if (id == null) throw new IllegalArgumentException("El ID no puede ser nulo");
        Beneficiario b = beneficiarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Beneficiario no encontrado"));
        // Prevent delete if titular de grupo familiar (business rule)
        if (b.getGrupoFamiliar() != null) {
            throw new IllegalArgumentException("No se puede eliminar el beneficiario porque es titular de un grupo familiar. Elimine o reasigne el grupo primero.");
        }
        super.eliminar(id);
    }

    @Transactional(readOnly = true)
    public List<Beneficiario> listarPorEmpresaId(Long empresaId) throws Exception {
        if (empresaId == null) throw new IllegalArgumentException("El id de empresa no puede ser nulo");
        Empresa empresa = empresaRepository.findById(empresaId).orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));
        return beneficiarioRepository.findByEmpresa(empresa);
    }

    @Transactional(readOnly = true)
    public Optional<Beneficiario> buscarPorDni(Integer dni) {
        if (dni == null) return Optional.empty();
        return beneficiarioRepository.findByPersona_Dni(dni);
    }

    @Transactional(readOnly = true)
    public List<Beneficiario> listarAfiliadosSindicato() {
        return beneficiarioRepository.findByAfiliadoSindicalTrue();
    }
}
