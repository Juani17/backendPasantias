package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Domicilio;
import com.Ospuaye.BackendOspuaye.Entity.Empresa;
import com.Ospuaye.BackendOspuaye.Repository.BeneficiarioRepository;
import com.Ospuaye.BackendOspuaye.Repository.DomicilioRepository;
import com.Ospuaye.BackendOspuaye.Repository.EmpresaRepository;
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
public class EmpresaService extends BaseService<Empresa, Long> {

    private final EmpresaRepository empresaRepository;
    private final BeneficiarioRepository beneficiarioRepository;
    private final DomicilioRepository domicilioRepository;


    public EmpresaService(EmpresaRepository empresaRepository,
                          BeneficiarioRepository beneficiarioRepository, DomicilioRepository domicilioRepository) {
        super(empresaRepository);
        this.empresaRepository = empresaRepository;
        this.beneficiarioRepository = beneficiarioRepository;
        this.domicilioRepository = domicilioRepository;
    }

    // ===============================================
    // BUSQUEDA GLOBAL + PAGINADO (ACTIVOS)
    // ===============================================
    @Transactional(readOnly = true)
    public Page<Empresa> buscar(String query, int page, int size) {
        if (page < 0) page = 0;
        if (size <= 0) size = 5;

        Pageable pageable = PageRequest.of(page, size);

        if (query == null || query.trim().isEmpty()) {
            return super.paginar(page, size); // ✅ activos por defecto
        }

        String q = query.trim();
        return empresaRepository.findByCuitContainingIgnoreCaseAndActivoTrueOrRazonSocialContainingIgnoreCaseAndActivoTrueOrDomicilio_CalleContainingIgnoreCaseAndActivoTrue(
                q, q, q, pageable);
    }

    // ===============================================
    // BUSQUEDA GLOBAL + PAGINADO (INACTIVOS)
    // ===============================================
    @Transactional(readOnly = true)
    public Page<Empresa> buscarInactivos(String query, int page, int size) {
        if (page < 0) page = 0;
        if (size <= 0) size = 5;

        Pageable pageable = PageRequest.of(page, size);

        if (query == null || query.trim().isEmpty()) {
            return super.paginarInactivos(page, size); // ✅ inactivos por defecto
        }

        String q = query.trim();
        return empresaRepository.findByCuitContainingIgnoreCaseAndActivoFalseOrRazonSocialContainingIgnoreCaseAndActivoFalseOrDomicilio_CalleContainingIgnoreCaseAndActivoFalse(
                q, q, q, pageable);
    }


    @Override
    @Transactional
    public Empresa crear(Empresa entity) throws Exception {
        if (entity == null) throw new IllegalArgumentException("La empresa no puede ser nula");
        if (entity.getCuit() == null || entity.getCuit().isBlank())
            throw new IllegalArgumentException("El CUIT es obligatorio");
        String cuit = entity.getCuit().trim();
        if (!cuit.matches("\\d{11}"))
            throw new IllegalArgumentException("CUIT inválido (debe tener 11 dígitos)");
        if (empresaRepository.existsByCuit(cuit))
            throw new IllegalArgumentException("Ya existe una empresa con ese CUIT");

        if (entity.getRazonSocial() == null || entity.getRazonSocial().isBlank())
            throw new IllegalArgumentException("La razón social es obligatoria");

        entity.setCuit(cuit);
        if (entity.getActivo() == null) entity.setActivo(true);
        if (entity.getDomicilio() != null && entity.getDomicilio().getId() != null) {
            Domicilio domicilio = domicilioRepository.findById(entity.getDomicilio().getId())
                    .orElseThrow(() -> new RuntimeException("Domicilio no encontrado"));
            entity.setDomicilio(domicilio);
        }

        return empresaRepository.save(entity);
    }

    @Override
    @Transactional
    public Empresa actualizar(Empresa entity) throws Exception {
        if (entity == null || entity.getId() == null)
            throw new IllegalArgumentException("La empresa o su ID no pueden ser nulos");

        Empresa existente = empresaRepository.findById(entity.getId())
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));

        // Validar CUIT
        if (entity.getCuit() != null && !entity.getCuit().isBlank()) {
            String nuevoCuit = entity.getCuit().trim();
            if (!nuevoCuit.matches("\\d{11}"))
                throw new IllegalArgumentException("CUIT inválido (11 dígitos)");
            if (!nuevoCuit.equals(existente.getCuit()) && empresaRepository.existsByCuit(nuevoCuit))
                throw new IllegalArgumentException("Otro registro ya utiliza ese CUIT");
            existente.setCuit(nuevoCuit);
        }

        // Validar razón social
        if (entity.getRazonSocial() != null && !entity.getRazonSocial().isBlank()) {
            existente.setRazonSocial(entity.getRazonSocial().trim());
        }

        // Validar estado activo/desactivo
        if (entity.getActivo() != null) {
            if (!entity.getActivo()) {
                var asociados = beneficiarioRepository.findByEmpresa(existente);
                if (!asociados.isEmpty()) {
                    throw new IllegalArgumentException("No se puede desactivar la empresa porque tiene beneficiarios asociados");
                }
            }
            existente.setActivo(entity.getActivo());
        }

        return empresaRepository.save(existente);
    }

    @Transactional(readOnly = true)
    public List<Empresa> listarPorActivo(Boolean activo) {
        return empresaRepository.findByActivo(activo);
    }

    @Override
    @Transactional
    public void eliminar(Long id) throws Exception {
        if (id == null) throw new IllegalArgumentException("El ID no puede ser nulo");
        Empresa e = empresaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));

        var asociados = beneficiarioRepository.findByEmpresa(e);
        if (!asociados.isEmpty()) {
            throw new IllegalArgumentException("No se puede eliminar la empresa porque tiene beneficiarios asociados.");
        }
        super.eliminar(id);
    }
    @Transactional(readOnly = true)
    public Optional<Empresa> buscarPorCuit(String cuit) throws Exception {
        if (cuit == null || cuit.trim().isEmpty()) {
            throw new IllegalArgumentException("El CUIT no puede ser nulo ni vacío");
        }

        Optional<Empresa> empresa = empresaRepository.findByCuit(cuit);

        if (empresa.isEmpty()) {
            throw new IllegalArgumentException("No se encontró una empresa con el CUIT especificado");
        }

        return empresa;
    }

    @Transactional(readOnly = true)
    public Optional<Empresa> buscarPorCuit2(String cuit) {
        if (cuit == null || cuit.trim().isEmpty()) {
            return Optional.empty();
        }
        return empresaRepository.findByCuit(cuit.trim());
    }

    @Transactional(readOnly = true)
    public ByteArrayResource exportarTXT() {

        List<Empresa> empresas = empresaRepository.findAll();
        StringBuilder sb = new StringBuilder();

        // CABECERA — alineada con los mismos pad() que los datos
        sb.append(
                pad("id", 5) + "|" +
                        pad("cuit", 12) + "|" +
                        pad("razonSocial", 30) + "|" +
                        pad("domicilio", 40) + "|" +
                        pad("beneficiarios", 15) + "|" +
                        pad("activo", 8)
        ).append("\n");

        // DATOS
        for (Empresa e : empresas) {

            // Domicilio en un solo texto
            String domicilioStr = "SIN_DOMICILIO";
            if (e.getDomicilio() != null) {
                Domicilio d = e.getDomicilio();
                domicilioStr =
                        safe(d.getCalle()) + " " +
                                safe(d.getNumeracion()) + " - " +
                                (d.getLocalidad() != null ? safe(d.getLocalidad().getNombre()) : "");
            }

            String linea =
                    pad(safe(e.getId()), 5) + "|" +
                            pad(safe(e.getCuit()), 12) + "|" +
                            pad(safe(e.getRazonSocial()), 30) + "|" +
                            pad(domicilioStr, 40) + "|" +
                            pad(String.valueOf(e.getBeneficiarios() != null ? e.getBeneficiarios().size() : 0), 15) + "|" +
                            pad(String.valueOf(e.getActivo()), 8);

            sb.append(linea).append("\n");
        }

        return new ByteArrayResource(sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    // Evita nulls
    private String safe(Object o) {
        return o == null ? "" : o.toString();
    }

    // Alinea columnas agregando espacios
    private String pad(String txt, int length) {
        if (txt == null) txt = "";
        if (txt.length() >= length) return txt;
        return txt + " ".repeat(length - txt.length());
    }



}
