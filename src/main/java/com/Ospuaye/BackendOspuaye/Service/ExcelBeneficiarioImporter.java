package com.Ospuaye.BackendOspuaye.Service;

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
    private final LocalidadService localidadService;
    private final EmpresaService empresaService;
    private final BeneficiarioService beneficiarioService;
    private final GrupoFamiliarService grupoFamiliarService;
    private final DepartamentoService departamentoService;
    private final NacionalidadService nacionalidadService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public ExcelBeneficiarioImporter(BeneficiarioRepository beneficiarioRepository,
                                     GrupoFamiliarRepository grupoFamiliarRepository,
                                     EmpresaRepository empresaRepository,
                                     DomicilioRepository domicilioRepository,
                                     NacionalidadRepository nacionalidadRepository,
                                     LocalidadRepository localidadRepository,
                                     LocalidadService localidadService,
                                     EmpresaService empresaService,
                                     BeneficiarioService beneficiarioService,
                                     GrupoFamiliarService grupoFamiliarService,
                                     DepartamentoService departamentoService,
                                     NacionalidadService nacionalidadService) {
        this.beneficiarioRepository = beneficiarioRepository;
        this.grupoFamiliarRepository = grupoFamiliarRepository;
        this.empresaRepository = empresaRepository;
        this.domicilioRepository = domicilioRepository;
        this.nacionalidadRepository = nacionalidadRepository;
        this.localidadRepository = localidadRepository;
        this.localidadService = localidadService;
        this.empresaService = empresaService;
        this.beneficiarioService = beneficiarioService;
        this.grupoFamiliarService = grupoFamiliarService;
        this.departamentoService = departamentoService;
        this.nacionalidadService = nacionalidadService;
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

        // Mapa para no buscar repetidamente el titular en Excel grandes
        Map<Long, GrupoFamiliar> grupoPorTitular = new HashMap<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            StringTokenizer st = new StringTokenizer(line, "|");

            String rnos = st.nextToken();
            String cuitEmpresa = st.nextToken();
            String cuilTitular = st.nextToken();
            String tipoParentescoStr = st.nextToken(); // <-- clave
            st.nextToken(); // Cuil Familiar, ignorado
            String tipoDocumento = st.nextToken();
            st.nextToken(); // Documento, ignorado
            String nombreCompleto = st.nextToken();
            String sexoStr = st.nextToken();
            String estadoCivilStr = st.nextToken();
            String fechaNacimientoStr = st.nextToken();
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
            String situacionRevista = st.nextToken();
            String incapacidadStr = st.nextToken();
            String tipoBeneficiarioStr = st.nextToken();
            String fechaAltaOSStr = st.nextToken();

            TipoParentesco tipoParentesco = TipoParentesco.Sin_Informacion;
            if (tipoParentescoStr != null && !tipoParentescoStr.isBlank()) {
                try {
                    tipoParentesco = TipoParentesco.valueOf(tipoParentescoStr.replace(" ", "_"));
                } catch (Exception ignored) {}
            }

            // --- Empresa ---
            Empresa empresa = null;
            if (cuitEmpresa != null && !cuitEmpresa.isBlank()) {
                try {
                    empresa = empresaService.buscarPorCuit2(cuitEmpresa);
                } catch (Exception e) {
                    empresa = Empresa.builder()
                            .cuit(cuitEmpresa)
                            .activo(true)
                            .beneficiarios(new HashSet<>())
                            .razonSocial("Indefinida")
                            .build();
                    empresaRepository.save(empresa);
                }
            }

            // --- Persona base: nombre, apellido, cuil, dni, etc ---
            String nombre = nombreCompleto.split(" ")[0];
            String apellido = nombreCompleto.contains(" ") ? nombreCompleto.substring(nombreCompleto.indexOf(" ") + 1) : "";
            Long telefono;
            if (telefonoStr != null && !telefonoStr.isBlank()) {
                // Eliminamos todos los espacios
                telefonoStr = telefonoStr.replaceAll("\\s+", "");
                try {
                    telefono = Long.parseLong(telefonoStr);
                } catch (NumberFormatException e) {
                    telefono = null; // O loguear que el número no es válido
                }
            } else {
                telefono = null;
            }

            // --- Fecha de nacimiento ---
            Date fechaNacimiento = null;
            if (fechaNacimientoStr != null && !fechaNacimientoStr.isBlank()) {
                try {
                    fechaNacimiento = dateFormat.parse(fechaNacimientoStr);
                } catch (Exception e) {
                    System.out.println("Fecha de nacimiento inválida para CUIL " + cuilTitular + ": " + fechaNacimientoStr);
                    fechaNacimiento = null; // o new Date(0) si querés un valor por defecto
                }
            }
            Sexo sexo = sexoStr.equalsIgnoreCase("MASCULINO") ? Sexo.MASCULINO :
                    sexoStr.equalsIgnoreCase("FEMENINO") ? Sexo.FEMENINO : Sexo.SIN_INFORMACION;

            // --- Nacionalidad ---
            Nacionalidad nacionalidad = null;
            if (nacionalidadNombre != null && !nacionalidadNombre.isBlank()) {
                nacionalidad = nacionalidadService.ListarPorNombre(nacionalidadNombre)
                        .orElse(Nacionalidad.builder()
                                .nombre(nacionalidadNombre)
                                .activo(true)
                                .build());
                nacionalidadRepository.save(nacionalidad);
            }

            // --- Provincia / Departamento ---
            Departamento departamentoObj = null;
            if (provincia != null && !provincia.isBlank()) {
                departamentoObj = departamentoService.ListarPorNombre(provincia);
            }

            // --- Localidad ---
            Localidad localidad = null;
            if (localidadNombre != null && !localidadNombre.isBlank()) {
                localidad = localidadService.listarPorDepartamentoYNombre(localidadNombre,
                                (departamentoObj != null ? departamentoObj.getId() : null))
                        .orElse(Localidad.builder()
                                .nombre(localidadNombre)
                                .codigoPostal(codigoPostal)
                                .departamento((departamentoService.ListarPorNombre(departamento)))
                                .activo(true)
                                .build());
                localidadRepository.save(localidad);
            }

            // --- Domicilio ---
            Domicilio domicilio = Domicilio.builder()
                    .calle(calle)
                    .numeracion(puerta)
                    .manzanaPiso(piso)
                    .casaDepartamento(departamento)
                    .localidad(localidad)
                    .tipoDomicilio(tipoDomicilioStr != null && tipoDomicilioStr.equalsIgnoreCase("DOMICILIO_RURAL")
                            ? TipoDeDomicilio.DOMICILIO_RURAL
                            : TipoDeDomicilio.DOMICILIO_COMPLETO)
                    .activo(true)
                    .build();
            domicilioRepository.save(domicilio);

            if (tipoParentesco == TipoParentesco.Titular) {
                // --- Beneficiario titular ---
                Beneficiario titular = beneficiarioService.ListarPorCuil(Long.parseLong(cuilTitular))
                        .orElse(Beneficiario.builder()
                                .nombre(nombre)
                                .apellido(apellido)
                                .cuil(Long.parseLong(cuilTitular))
                                .telefono(telefono)
                                .sexo(sexo)
                                .empresa(empresa)
                                .afiliadoSindical(false)
                                .esJubilado(false)
                                .estadoCivil((estadoCivilStr != null && !estadoCivilStr.isBlank())
                                        ? EstadoCivil.valueOf(estadoCivilStr.replace(" ", "_").replace("-", "_"))
                                        : EstadoCivil.Sin_Informacion)
                                .fechaNacimiento(fechaNacimiento)
                                .nacionalidad(nacionalidad)
                                .domicilio(domicilio)
                                .build());

                beneficiarioRepository.save(titular);


                // --- Fecha de alta segura ---
                Date fechaAlta = new Date(); // por defecto hoy
                if (fechaAltaOSStr != null && !fechaAltaOSStr.isBlank()) {
                    try {
                        String[] partes = fechaAltaOSStr.split("\\s+");
                        fechaAlta = new SimpleDateFormat("ddMMyyyy").parse(partes[0]);
                    } catch (Exception e) {
                        System.out.println("Fecha de alta inválida para CUIL " + titular.getCuil() + ": " + fechaAltaOSStr);
                        // queda fechaAlta = hoy
                    }
                }

                // --- Crear Grupo Familiar ---
                GrupoFamiliar grupo = grupoFamiliarService.buscarPorTitularActivo(titular.getId())
                        .orElse(GrupoFamiliar.builder()
                                .titular(titular)
                                .tipoBeneficiarioTitular(TipoDeBeneficiarioTitular.SIN_INFORMACION)
                                .nombreGrupo(titular.getNombre() + " " + titular.getApellido())
                                .activo(true)
                                .fechaAlta(fechaAlta) // <- acá usamos la variable
                                .familiares(new ArrayList<>())
                                .build());
                grupoFamiliarRepository.save(grupo);

                grupoPorTitular.put(titular.getCuil(), grupo);

            } else {
                // --- Familiar ---
                GrupoFamiliar grupo = grupoPorTitular.get(Long.parseLong(cuilTitular));
                if (grupo == null) {
                    throw new Exception("No se encontró grupo familiar para CUIL titular: " + cuilTitular);
                }

                Familiar familiar = Familiar.builder()
                        .nombre(nombre)
                        .apellido(apellido)
                        .cuil(Long.parseLong(cuilTitular)) // Si el Excel tiene cuil del familiar distinto, usarlo
                        .telefono(telefono)
                        .sexo(sexo)
                        .nacionalidad(nacionalidad)
                        .grupoFamiliar(grupo)
                        .tipoParentesco(tipoParentesco)
                        .build();

                grupo.getFamiliares().add(familiar);
                grupoFamiliarRepository.save(grupo);
            }
        }

        scanner.close();
    }
}
