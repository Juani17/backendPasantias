package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Beneficiario;
import com.Ospuaye.BackendOspuaye.Entity.Familiar;
import com.Ospuaye.BackendOspuaye.Entity.GrupoFamiliar;
import com.Ospuaye.BackendOspuaye.Repository.BeneficiarioRepository;
import com.Ospuaye.BackendOspuaye.Repository.GrupoFamiliarRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Service
public class GrupoFamiliarService extends BaseService<GrupoFamiliar, Long> {

    private final GrupoFamiliarRepository grupoFamiliarRepository;
    private final BeneficiarioRepository beneficiarioRepository;

    public GrupoFamiliarService(GrupoFamiliarRepository repository,
                                BeneficiarioRepository beneficiarioRepository) {
        super(repository);
        this.grupoFamiliarRepository = repository;
        this.beneficiarioRepository = beneficiarioRepository;
    }

    // ===============================================
    // BUSQUEDA GLOBAL + PAGINADO (ACTIVOS)
    // ===============================================
    @Transactional(readOnly = true)
    public Page<GrupoFamiliar> buscar(String query, int page, int size) {
        if (page < 0) page = 0;
        if (size <= 0) size = 5;

        Pageable pageable = PageRequest.of(page, size);

        if (query == null || query.trim().isEmpty()) {
            return super.paginar(page, size); // ✅ activos por defecto
        }

        String q = query.trim();
        return grupoFamiliarRepository.findByNombreGrupoContainingIgnoreCaseAndActivoTrueOrTitular_NombreContainingIgnoreCaseAndActivoTrueOrTitular_ApellidoContainingIgnoreCaseAndActivoTrue(
                q, q, q, pageable);
    }

    // ===============================================
    // BUSQUEDA GLOBAL + PAGINADO (INACTIVOS)
    // ===============================================
    @Transactional(readOnly = true)
    public Page<GrupoFamiliar> buscarInactivos(String query, int page, int size) {
        if (page < 0) page = 0;
        if (size <= 0) size = 5;

        Pageable pageable = PageRequest.of(page, size);

        if (query == null || query.trim().isEmpty()) {
            return super.paginarInactivos(page, size); // ✅ inactivos por defecto
        }

        String q = query.trim();
        return grupoFamiliarRepository.findByNombreGrupoContainingIgnoreCaseAndActivoFalseOrTitular_NombreContainingIgnoreCaseAndActivoFalseOrTitular_ApellidoContainingIgnoreCaseAndActivoFalse(
                q, q, q, pageable);
    }


    @Override
    public GrupoFamiliar crear(GrupoFamiliar gf) throws Exception {
        validar(gf, null);
        if (gf.getFechaAlta() == null) {
            gf.setFechaAlta(new Date());
        }
        if (gf.getActivo() == null) gf.setActivo(true);
        return grupoFamiliarRepository.save(gf);
    }

    @Override
    public GrupoFamiliar actualizar(GrupoFamiliar gf) throws Exception {
        if (gf.getId() == null || !grupoFamiliarRepository.existsById(gf.getId())) {
            throw new Exception("Grupo familiar no encontrado");
        }
        validar(gf, gf.getId());
        return grupoFamiliarRepository.save(gf);
    }

    private void validar(GrupoFamiliar gf, Long idActual) throws Exception {
        if (gf.getNombreGrupo() == null || gf.getNombreGrupo().isBlank()) {
            throw new Exception("El nombre del grupo es obligatorio");
        }

        Beneficiario titular = gf.getTitular();
        if (titular == null || titular.getId() == null || !beneficiarioRepository.existsById(titular.getId())) {
            throw new Exception("El titular del grupo no existe");
        }

        // Un titular no debería tener dos grupos activos
        var existente = grupoFamiliarRepository.findByTitularId(titular.getId());
        if (existente.isPresent() && (idActual == null || !existente.get().getId().equals(idActual))) {
            throw new Exception("El titular ya posee un grupo familiar activo");
        }

        // Nombre único por titular
        if (grupoFamiliarRepository.existsByNombreGrupoAndTitularId(gf.getNombreGrupo(), titular.getId())) {
            throw new Exception("El titular ya tiene un grupo con ese nombre");
        }
    }

    public Optional<GrupoFamiliar> buscarPorTitularActivo(Long titularId) throws Exception {
        if (titularId == null) {
            throw new IllegalArgumentException("El Id no puede ser nulo");
        }

        Optional<GrupoFamiliar> grupoFamiliar = grupoFamiliarRepository.findByTitularIdAndActivoTrue(titularId);

        if (grupoFamiliar.isEmpty()) {
            throw new IllegalArgumentException("No se encontró un Titular con el id especificado");
        }

        return grupoFamiliar;
    }

    //metodod e exportar txt
    @Transactional(readOnly = true)
    public ByteArrayResource exportarTXT(Long grupoId) {

        GrupoFamiliar g = grupoFamiliarRepository.findById(grupoId)
                .orElseThrow(() -> new RuntimeException("Grupo familiar no encontrado"));

        StringBuilder sb = new StringBuilder();

        // ================= TITULAR =====================
        sb.append("=== TITULAR ===\n");
        sb.append(
                pad("id", 6) + "|" +
                        pad("nombre", 15) + "|" +
                        pad("apellido", 15) + "|" +
                        pad("dni", 12) + "|" +
                        pad("cuil", 15) + "|" +
                        pad("correo", 30) + "|" +
                        pad("telefono", 15) + "|" +
                        pad("fechaNacimiento", 20)
        ).append("\n");

        var t = g.getTitular();
        String correoTit = t.getUsuario() != null ? safe(t.getUsuario().getEmail()) : "";

        sb.append(
                pad(t.getId(), 6) + "|" +
                        pad(t.getNombre(), 15) + "|" +
                        pad(t.getApellido(), 15) + "|" +
                        pad(t.getDni(), 12) + "|" +
                        pad(t.getCuil(), 15) + "|" +
                        pad(correoTit, 30) + "|" +
                        pad(t.getTelefono(), 15) + "|" +
                        pad(t.getFechaNacimiento(), 20)
        ).append("\n\n");

        // ================= FAMILIARES =====================
        sb.append("=== FAMILIARES ===\n");
        sb.append(
                pad("id", 6) + "|" +
                        pad("nombre", 15) + "|" +
                        pad("apellido", 15) + "|" +
                        pad("dni", 12) + "|" +
                        pad("cuil", 15) + "|" +
                        pad("parentesco", 15) + "|" +
                        pad("correo", 30) + "|" +
                        pad("telefono", 15) + "|" +
                        pad("fechaNacimiento", 20)
        ).append("\n");

        for (Familiar f : g.getFamiliares()) {

            sb.append(
                    pad(f.getId(), 6) + "|" +
                            pad(f.getNombre(), 15) + "|" +
                            pad(f.getApellido(), 15) + "|" +
                            pad(f.getDni(), 12) + "|" +
                            pad(f.getCuil(), 15) + "|" +
                            pad(f.getTipoParentesco(), 15) + "|" +
                            pad(f.getCorreoElectronico(), 30) + "|" +
                            pad(f.getTelefono(), 15) + "|" +
                            pad(f.getFechaNacimiento(), 20)
            ).append("\n");
        }

        return new ByteArrayResource(sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    private String safe(Object o) {
        return o == null ? "" : o.toString();
    }

    private String pad(Object o, int length) {
        String s = safe(o);
        return String.format("%-" + length + "s", s);
    }


}
