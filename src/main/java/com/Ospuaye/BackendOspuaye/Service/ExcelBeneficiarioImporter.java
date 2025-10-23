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
    private final ProvinciaService provinciaService;
    private final ProvinciaRepository provinciaRepository;
    private final DepartamentoRepository departamentoRepository;

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
                                     NacionalidadService nacionalidadService,
                                     ProvinciaService provinciaService,
                                     ProvinciaRepository provinciaRepository,
                                     DepartamentoRepository departamentoRepository) {
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
        this.provinciaService = provinciaService;
        this.provinciaRepository = provinciaRepository;
        this.departamentoRepository = departamentoRepository;
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

        List<String> lineasTitulares = new ArrayList<>();
        List<String> lineasFamiliares = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            StringTokenizer stTemp = new StringTokenizer(line, "|");
            if (stTemp.countTokens() < 4) continue;

            stTemp.nextToken(); // rnos
            stTemp.nextToken(); // cuit empresa
            stTemp.nextToken(); // cuil titular
            String tipoParentescoStr = stTemp.nextToken().trim();

            int codigoParentesco = -1;
            try {
                codigoParentesco = Integer.parseInt(tipoParentescoStr);
            } catch (NumberFormatException ignored) {}

            if (codigoParentesco == 0) lineasTitulares.add(line);
            else lineasFamiliares.add(line);
        }

        scanner.close();

        Map<Long, GrupoFamiliar> grupoPorTitular = new HashMap<>();

        for (String line : lineasTitulares) procesarLinea(line, grupoPorTitular, true);
        for (String line : lineasFamiliares) procesarLinea(line, grupoPorTitular, false);
    }

    private void procesarLinea(String line, Map<Long, GrupoFamiliar> grupoPorTitular, boolean esTitular) throws Exception {
        StringTokenizer st = new StringTokenizer(line, "|");

        String rnos = st.nextToken();
        String cuitEmpresa = st.nextToken();
        String cuilTitular = st.nextToken();
        String tipoParentescoStr = st.nextToken();
        st.nextToken(); // cuil familiar
        st.nextToken(); // tipoDocumento
        st.nextToken(); // documento
        String nombreCompleto = st.nextToken();
        String sexoStr = st.nextToken();
        String estadoCivilStr = st.nextToken();
        String fechaNacimientoStr = st.nextToken();
        st.nextToken(); // edad
        String nacionalidadNombre = st.nextToken();
        String calle = st.nextToken();
        String puerta = st.nextToken();
        String piso = st.nextToken();
        String departamento1 = st.nextToken();
        String localidadNombre = st.nextToken();
        String codigoPostal = st.nextToken();
        String provincia1 = st.nextToken();
        String tipoDomicilioStr = st.nextToken();
        String telefonoStr = st.nextToken();
        String situacionRevista = st.nextToken();
        String incapacidadStr = st.nextToken();
        String tipoBeneficiarioStr = st.nextToken();
        String fechaAltaOSStr = st.hasMoreTokens() ? st.nextToken() : "";

        // --- Tipo Parentesco ---
        TipoParentesco tipoParentesco = TipoParentesco.Sin_Informacion;
        switch (tipoParentescoStr) {
            case "0" -> tipoParentesco = TipoParentesco.Titular;
            case "1" -> tipoParentesco = TipoParentesco.Conyuge;
            case "2" -> tipoParentesco = TipoParentesco.Concubino_Concubina;
            case "3" -> tipoParentesco = TipoParentesco.Hijo_Soltero_Menor_De_21;
            case "4" -> tipoParentesco = TipoParentesco.Hijo_Soltero_Entre_21_25_Estudiando;
            case "5" -> tipoParentesco = TipoParentesco.Hijo_Conyuge_Menor_De_21;
            case "6" -> tipoParentesco = TipoParentesco.Hijo_Conyuge_Entre_21_25_Estudiando;
            case "7" -> tipoParentesco = TipoParentesco.Menor_Bajo_Guarda_Tutela;
            case "8" -> tipoParentesco = TipoParentesco.Familiar_A_Cargo;
            case "9" -> tipoParentesco = TipoParentesco.Mayor_de_25_Discapacitado;
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

        // --- Nombre y Apellido ---
        String apellido = nombreCompleto.split(" ")[0];
        String nombre = nombreCompleto.contains(" ") ? nombreCompleto.substring(nombreCompleto.indexOf(" ") + 1) : "";

        // --- Telefono ---
        Long telefono = null;
        if (telefonoStr != null && !telefonoStr.isBlank()) {
            telefonoStr = telefonoStr.replaceAll("\\s+", "");
            try {
                telefono = Long.parseLong(telefonoStr);
            } catch (NumberFormatException ignored) {}
        }

        // --- Fecha de Nacimiento ---
        Date fechaNacimiento = null;
        if (fechaNacimientoStr != null && !fechaNacimientoStr.isBlank()) {
            try {
                fechaNacimientoStr = fechaNacimientoStr.trim().replaceAll("[^0-9]", "");
                if (fechaNacimientoStr.matches("\\d{8}")) {
                    fechaNacimientoStr = fechaNacimientoStr.substring(0, 2) + "/" +
                            fechaNacimientoStr.substring(2, 4) + "/" +
                            fechaNacimientoStr.substring(4);
                }
                fechaNacimiento = dateFormat.parse(fechaNacimientoStr);
            } catch (Exception ignored) {}
        }

        // --- Sexo ---
        Sexo sexo = Sexo.SIN_INFORMACION;
        if (sexoStr != null && !sexoStr.isBlank()) {
            sexoStr = sexoStr.trim().toUpperCase();
            if (sexoStr.equals("M")) sexo = Sexo.MASCULINO;
            else if (sexoStr.equals("F")) sexo = Sexo.FEMENINO;
            else if (sexoStr.equalsIgnoreCase("MASCULINO")) sexo = Sexo.MASCULINO;
            else if (sexoStr.equalsIgnoreCase("FEMENINO")) sexo = Sexo.FEMENINO;
        }

        // --- Nacionalidad ---
        Nacionalidad nacionalidad = null;
        if (nacionalidadNombre != null && !nacionalidadNombre.isBlank()) {
            nacionalidad = nacionalidadService.ListarPorId(Long.parseLong(nacionalidadNombre.trim()))
                    .orElse(Nacionalidad.builder()
                            .nombre(nacionalidadNombre)
                            .activo(true)
                            .build());
            nacionalidadRepository.save(nacionalidad);
        }

        // --- Provincia ---
        Provincia provincia = null;
        if (provincia1 != null && !provincia1.isBlank()) {
            provincia = provinciaService.ListarPorId(Long.parseLong(provincia1.trim()))
                    .orElse(Provincia.builder()
                            .nombre(provincia1)
                            .build());
            provinciaRepository.save(provincia);
        }

        // --- Departamento y Localidad ---
        Departamento departamento = null;
        Localidad localidad = null;

        if (localidadNombre != null && !localidadNombre.isBlank()) {
            Optional<Departamento> depOptional = departamentoRepository.findByNombre(localidadNombre);
            if (depOptional.isPresent()) departamento = depOptional.get();
            else {
                departamento = Departamento.builder()
                        .nombre(localidadNombre)
                        .activo(true)
                        .build();
                departamentoRepository.save(departamento);
            }

            Optional<Localidad> locOptional = localidadRepository.findByDepartamento_IdAndNombre(departamento.getId(), localidadNombre);
            if (locOptional.isPresent()) localidad = locOptional.get();
            else {
                localidad = Localidad.builder()
                        .nombre(localidadNombre)
                        .codigoPostal(codigoPostal)
                        .departamento(departamento)
                        .activo(true)
                        .build();
                localidadRepository.save(localidad);
            }
        }

        // --- Domicilio ---
        Domicilio domicilio = Domicilio.builder()
                .calle(calle)
                .numeracion(puerta)
                .manzanaPiso(piso)
                .casaDepartamento(departamento1)
                .localidad(localidad)
                .tipoDomicilio(tipoDomicilioStr != null && tipoDomicilioStr.equalsIgnoreCase("DOMICILIO_RURAL")
                        ? TipoDeDomicilio.DOMICILIO_RURAL
                        : TipoDeDomicilio.DOMICILIO_COMPLETO)
                .activo(true)
                .build();
        domicilioRepository.save(domicilio);

        if (tipoParentesco == TipoParentesco.Titular) {
            // --- Titular ---
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

            Date fechaAlta = new Date();
            if (fechaAltaOSStr != null && !fechaAltaOSStr.isBlank()) {
                try {
                    fechaAlta = new SimpleDateFormat("ddMMyyyy").parse(fechaAltaOSStr);
                } catch (Exception ignored) {}
            }

            GrupoFamiliar grupo = grupoFamiliarService.buscarPorTitularActivo(titular.getId())
                    .orElse(GrupoFamiliar.builder()
                            .titular(titular)
                            .tipoBeneficiarioTitular(TipoDeBeneficiarioTitular.SIN_INFORMACION)
                            .nombreGrupo(titular.getNombre() + " " + titular.getApellido())
                            .activo(true)
                            .fechaAlta(fechaAlta)
                            .familiares(new ArrayList<>())
                            .build());
            grupoFamiliarRepository.save(grupo);
            grupoPorTitular.put(titular.getCuil(), grupo);

        } else {
            // --- Familiar ---
            GrupoFamiliar grupo = grupoPorTitular.get(Long.parseLong(cuilTitular));
            if (grupo == null) throw new Exception("No se encontró grupo familiar para CUIL titular: " + cuilTitular);

            Familiar familiar = Familiar.builder()
                    .nombre(nombre)
                    .apellido(apellido)
                    .cuil(Long.parseLong(cuilTitular))
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
}
