package com.Ospuaye.BackendOspuaye.Util;

import com.Ospuaye.BackendOspuaye.Entity.*;
import com.Ospuaye.BackendOspuaye.Entity.Enum.*;
import com.Ospuaye.BackendOspuaye.Repository.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ExcelBeneficiarioImporter {

    private final BeneficiarioRepository beneficiarioRepository;
    private final GrupoFamiliarRepository grupoFamiliarRepository;
    private final EmpresaRepository empresaRepository;
    private final DomicilioRepository domicilioRepository;
    private final NacionalidadRepository nacionalidadRepository;
    private final LocalidadRepository localidadRepository;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public ExcelBeneficiarioImporter(BeneficiarioRepository beneficiarioRepository,
                                     GrupoFamiliarRepository grupoFamiliarRepository,
                                     EmpresaRepository empresaRepository,
                                     DomicilioRepository domicilioRepository,
                                     NacionalidadRepository nacionalidadRepository,
                                     LocalidadRepository localidadRepository) {
        this.beneficiarioRepository = beneficiarioRepository;
        this.grupoFamiliarRepository = grupoFamiliarRepository;
        this.empresaRepository = empresaRepository;
        this.domicilioRepository = domicilioRepository;
        this.nacionalidadRepository = nacionalidadRepository;
        this.localidadRepository = localidadRepository;
    }

    @Transactional
    public void importar(String path) throws Exception {
        File file = new File(path);
        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new Exception("Archivo no encontrado: " + path);
        }

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            StringTokenizer st = new StringTokenizer(line, "\t");

            // --- Leer campos del Excel ---
            String cuitEmpresa = st.nextToken();
            String cuilTitular = st.nextToken();
            st.nextToken(); // Tipo Parentesco, ignorado
            st.nextToken(); // Cuil Familiar, ignorado
            String tipoDocumentoStr = st.nextToken();
            st.nextToken(); // Documento, ignorado
            String nombreCompleto = st.nextToken();
            String sexoStr = st.nextToken();
            String fechaNacimientoStr = st.nextToken();
            String edadStr = st.nextToken();
            String nacionalidadNombre = st.nextToken();
            String calle = st.nextToken();
            String puerta = st.nextToken();
            String piso = st.nextToken();
            String departamento = st.nextToken();
            String localidadNombre = st.nextToken();
            String codigoPostal = st.nextToken();
            String provincia = st.nextToken();
            String tipoDomicilioStr = st.nextToken();
            String telefonoStr = st.nextToken();
            String situacionRevista = st.nextToken(); // opcional
            String incapacidadStr = st.nextToken();   // opcional
            String tipoBeneficiarioStr = st.nextToken();
            String fechaAltaOSStr = st.nextToken();   // opcional
            st.hasMoreTokens(); st.nextToken(); // validaCuil, ignorado
            st.hasMoreTokens(); st.nextToken(); // cuilObraSocial, ignorado
            st.hasMoreTokens(); st.nextToken(); // tipoBeneficiarioInfOS, ignorado
            String cuitEmpleadorOS = st.hasMoreTokens() ? st.nextToken() : null;

            // --- Empresa ---
            Empresa empresa = null;
            if (cuitEmpleadorOS != null && !cuitEmpleadorOS.isBlank()) {
                empresa = empresaRepository.findByCuit(cuitEmpleadorOS)
                        .orElse(Empresa.builder()
                                .cuit(cuitEmpleadorOS)
                                .activo(true)
                                .beneficiarios(new HashSet<>())
                                .build());
                empresaRepository.save(empresa);
            }

            // --- Beneficiario ---
            Beneficiario titular = beneficiarioRepository.findByCuil(Long.parseLong(cuilTitular))
                    .orElse(Beneficiario.builder()
                            .cuil(Long.parseLong(cuilTitular))
                            .nombre(nombreCompleto.split(" ")[0])
                            .apellido(nombreCompleto.contains(" ") ? nombreCompleto.substring(nombreCompleto.indexOf(" ") + 1) : "")
                            .sexo(sexoStr.equalsIgnoreCase("MASCULINO") ? Sexo.MASCULINO :
                                    sexoStr.equalsIgnoreCase("FEMENINO") ? Sexo.FEMENINO : Sexo.SIN_INFORMACION)
                            .telefono(telefonoStr != null && !telefonoStr.isBlank() ? Long.parseLong(telefonoStr) : null)
                            .empresa(empresa)
                            .afiliadoSindical(false)
                            .esJubilado(false)
                            .build());

            if (fechaNacimientoStr != null && !fechaNacimientoStr.isBlank()) {
                titular.setFechaNacimiento(dateFormat.parse(fechaNacimientoStr));
            }
            if (edadStr != null && !edadStr.isBlank()) {
                try {
                    titular.setEdad(Integer.parseInt(edadStr));
                } catch (NumberFormatException ignored) {}
            }

            // --- Incapacidad ---
            if (incapacidadStr != null && !incapacidadStr.isBlank()) {
                titular.setIncapacidad(incapacidadStr.equalsIgnoreCase("INCAPACITADO") ? Incapacidad.INCAPACITADO : Incapacidad.NO_INCAPACITADO);
            }

            beneficiarioRepository.save(titular);

            // --- Grupo Familiar ---
            TipoDeBeneficiarioTitular tipoBeneficiarioTitular = tipoBeneficiarioStr != null ?
                    TipoDeBeneficiarioTitular.valueOf(tipoBeneficiarioStr) :
                    TipoDeBeneficiarioTitular.SIN_INFORMACION;

            Date fechaAltaOS = null;
            if (fechaAltaOSStr != null && !fechaAltaOSStr.isBlank()) {
                fechaAltaOS = dateFormat.parse(fechaAltaOSStr);
            }

            GrupoFamiliar grupo = grupoFamiliarRepository.findByTitularId(titular.getId())
                    .orElse(GrupoFamiliar.builder()
                            .titular(titular)
                            .tipoBeneficiarioTitular(tipoBeneficiarioTitular)
                            .nombreGrupo(titular.getNombre() + " " + titular.getApellido())
                            .activo(true)
                            .fechaAlta(fechaAltaOS != null ? fechaAltaOS : new Date())
                            .familiares(new ArrayList<>())
                            .build());
            grupoFamiliarRepository.save(grupo);

            // --- Nacionalidad ---
            Nacionalidad nacionalidad = null;
            if (nacionalidadNombre != null && !nacionalidadNombre.isBlank()) {
                nacionalidad = nacionalidadRepository.findByNombre(nacionalidadNombre)
                        .orElse(Nacionalidad.builder()
                                .nombre(nacionalidadNombre)
                                .activo(true)
                                .build());
                nacionalidadRepository.save(nacionalidad);
            }

            titular.setNacionalidad(nacionalidad);

            // --- Localidad ---
            Localidad localidad = null;
            if (localidadNombre != null && !localidadNombre.isBlank()) {
                localidad = localidadRepository.findByNombre(localidadNombre)
                        .orElse(Localidad.builder()
                                .nombre(localidadNombre)
                                .codigoPostal(codigoPostal)
                                .departamento(departamento)
                                .activo(true)
                                .build());
                localidadRepository.save(localidad);
            }

            // --- Domicilio ---
            Domicilio domicilio = Domicilio.builder()
                    .calle(calle)
                    .numeracion(puerta)
                    .casaDepartamento(departamento)
                    .localidad(localidad)
                    .tipoDomicilio(tipoDomicilioStr != null && tipoDomicilioStr.equalsIgnoreCase("RURAL") ? TipoDeDomicilio.DOMICILIO_RURAL : TipoDeDomicilio.DOMICILIO_COMPLETO)
                    .activo(true)
                    .build();

            domicilioRepository.save(domicilio);

            titular.setDomicilio(domicilio);
            beneficiarioRepository.save(titular);
        }

        scanner.close();
    }
}
