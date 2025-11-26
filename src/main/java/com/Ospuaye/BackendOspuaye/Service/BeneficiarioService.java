package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import com.Ospuaye.BackendOspuaye.Entity.Empresa;
import com.Ospuaye.BackendOspuaye.Repository.BeneficiarioRepository;
import com.Ospuaye.BackendOspuaye.Repository.EmpresaRepository;
import com.Ospuaye.BackendOspuaye.Repository.UsuarioRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
public class BeneficiarioService extends BaseService<Beneficiario, Long> {

    private final BeneficiarioRepository beneficiarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;

    public BeneficiarioService(BeneficiarioRepository beneficiarioRepository,
                               UsuarioRepository usuarioRepository,
                               EmpresaRepository empresaRepository) {
        super(beneficiarioRepository);
        this.beneficiarioRepository = beneficiarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.empresaRepository = empresaRepository;
    }

    // ===============================================
    // BUSQUEDA GLOBAL + PAGINADO
    // ===============================================
    @Transactional(readOnly = true)
    public Page<Beneficiario> buscar(String query, int page, int size) {
        if (page < 0) page = 0;
        if (size <= 0) size = 5;

        if (query == null || query.trim().isEmpty()) {
            return super.paginar(page, size); // ✅ paginado genérico
        }

        String q = query.trim();

        if (q.matches("\\d+")) {
            try {
                Long dni = Long.parseLong(q);
                return beneficiarioRepository.findByDni(dni, PageRequest.of(page, size));
            } catch (NumberFormatException ignored) {}
        }

        return beneficiarioRepository
                .findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(q, q, PageRequest.of(page, size));
    }

    // ===============================================
    // CREAR
    // ===============================================
    @Override
    @Transactional
    public Beneficiario crear(Beneficiario entity) throws Exception {
        if (entity == null) throw new IllegalArgumentException("El beneficiario no puede ser nulo");

        if (entity.getUsuario() == null || entity.getUsuario().getId() == null)
            throw new IllegalArgumentException("El usuario asociado es obligatorio");

        if (!usuarioRepository.existsById(entity.getUsuario().getId()))
            throw new IllegalArgumentException("El usuario asociado no existe");

        if (beneficiarioRepository.existsByUsuario_Id(entity.getUsuario().getId()))
            throw new IllegalArgumentException("Ya existe un beneficiario asociado a ese usuario");

        if (entity.getEmpresa() != null) {
            Long empresaId = entity.getEmpresa().getId();
            if (empresaId == null) throw new IllegalArgumentException("La empresa debe tener id");

            Empresa empresa = empresaRepository.findById(empresaId)
                    .orElseThrow(() -> new IllegalArgumentException("La empresa asociada no existe"));

            if (empresa.getActivo() != null && !empresa.getActivo())
                throw new IllegalArgumentException("No se puede asociar a una empresa inactiva");

            entity.setEmpresa(empresa);
        }

        if (entity.getAfiliadoSindical() == null) entity.setAfiliadoSindical(false);
        if (entity.getEsJubilado() == null) entity.setEsJubilado(false);

        return beneficiarioRepository.save(entity);
    }

    // ===============================================
    // ACTUALIZAR
    // ===============================================
    @Override
    @Transactional
    public Beneficiario actualizar(Beneficiario entity) throws Exception {
        if (entity == null || entity.getId() == null)
            throw new IllegalArgumentException("La entidad o su ID no pueden ser nulos");

        Beneficiario existente = beneficiarioRepository.findById(entity.getId())
                .orElseThrow(() -> new IllegalArgumentException("Beneficiario no encontrado"));

        if (entity.getUsuario() != null && entity.getUsuario().getId() != null) {
            Long nuevoUsuarioId = entity.getUsuario().getId();
            if (!usuarioRepository.existsById(nuevoUsuarioId))
                throw new IllegalArgumentException("El usuario asociado no existe");

            Optional<Beneficiario> byUser = beneficiarioRepository.findByUsuario_Id(nuevoUsuarioId);
            if (byUser.isPresent() && !byUser.get().getId().equals(entity.getId()))
                throw new IllegalArgumentException("El usuario ya está vinculado a otro beneficiario");

            existente.setUsuario(entity.getUsuario());
        }

        if (entity.getEmpresa() != null) {
            Long empresaId = entity.getEmpresa().getId();
            if (empresaId == null) throw new IllegalArgumentException("La empresa debe tener id");

            Empresa empresa = empresaRepository.findById(empresaId)
                    .orElseThrow(() -> new IllegalArgumentException("La empresa asociada no existe"));

            if (empresa.getActivo() != null && !empresa.getActivo())
                throw new IllegalArgumentException("No se puede asociar a una empresa inactiva");

            existente.setEmpresa(empresa);
        } else {
            existente.setEmpresa(null);
        }

        if (entity.getAfiliadoSindical() != null) existente.setAfiliadoSindical(entity.getAfiliadoSindical());
        if (entity.getEsJubilado() != null) existente.setEsJubilado(entity.getEsJubilado());

        return beneficiarioRepository.save(existente);
    }

    // ===============================================
    // ELIMINAR
    // ===============================================
    @Override
    @Transactional
    public void eliminar(Long id) throws Exception {
        if (id == null) throw new IllegalArgumentException("El ID no puede ser nulo");

        Beneficiario b = beneficiarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Beneficiario no encontrado"));

        if (b.getGrupoFamiliar() != null) {
            throw new IllegalArgumentException("No se puede eliminar el beneficiario porque es titular de un grupo familiar.");
        }

        super.eliminar(id);
    }

    // ===============================================
    // OTROS LISTADOS
    // ===============================================
    @Transactional(readOnly = true)
    public List<Beneficiario> listarPorEmpresaId(Long empresaId) throws Exception {
        if (empresaId == null) throw new IllegalArgumentException("El id de empresa no puede ser nulo");
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));
        return beneficiarioRepository.findByEmpresa(empresa);
    }

    @Transactional(readOnly = true)
    public Optional<Beneficiario> buscarPorDni(Long dni) {
        if (dni == null) return Optional.empty();
        return beneficiarioRepository.findByDni(dni);
    }

    @Transactional(readOnly = true)
    public Optional<Beneficiario> listarPorCuil(Long cuil) throws Exception {
        if (cuil == null) throw new IllegalArgumentException("El CUIL no puede ser nulo");
        Optional<Beneficiario> beneficiario = beneficiarioRepository.findByCuil(cuil);
        if (beneficiario.isEmpty()) throw new IllegalArgumentException("No se encontró un Beneficiario con el CUIL especificado");
        return beneficiario;
    }

    @Transactional(readOnly = true)
    public List<Beneficiario> listarAfiliadosSindicato() {
        return beneficiarioRepository.findByAfiliadoSindicalTrue();
    }
    @Transactional(readOnly = true)
    public List<Beneficiario> buscarSimple(String filtro) {

        if (filtro == null || filtro.trim().isEmpty()) {
            return List.of(); // ❌ No devolvemos miles de registros
        }

        String f = filtro.trim();

        Long dni = null;
        Long cuil = null;

        if (f.matches("\\d+")) {  // Si es número, lo usamos para dni/cuil
            try {
                Long num = Long.parseLong(f);
                dni = num;
                cuil = num;
            } catch (Exception ignored) {}
        }

        return beneficiarioRepository
                .findTop20ByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrDniEqualsOrCuilEquals(
                        f, f, dni, cuil
                );
    }


    //Agrego logica para descargar beneficiarios de la base
    @Transactional(readOnly = true)
    public ByteArrayResource exportarTXT() {

        List<Beneficiario> lista = beneficiarioRepository.findAll();

        StringBuilder sb = new StringBuilder();

        for (Beneficiario b : lista) {

            String linea = String.join("|",
                    safe(b.getId()),
                    safe(b.getNombre()),
                    safe(b.getApellido()),
                    safe(b.getDni()),
                    safe(b.getCuil()),
                    safe(b.getEmpresa() != null ? b.getEmpresa().getId() : ""),
                    safe(b.getCorreoElectronico()),
                    safe(b.getTelefono()),
                    safe(b.getFechaNacimiento())
            );

            sb.append(linea).append("\n");
        }

        return new ByteArrayResource(sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    private String safe(Object o) {
        return o == null ? "" : o.toString();
    }



}