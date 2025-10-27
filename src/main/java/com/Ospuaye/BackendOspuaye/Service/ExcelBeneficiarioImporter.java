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
    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PaisRepository paisRepository;

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
                                     DepartamentoRepository departamentoRepository, RolRepository rolRepository, UsuarioRepository usuarioRepository, PaisRepository paisRepository) {
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
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.paisRepository = paisRepository;
    }

    @Transactional
    public void importar(String path) throws Exception {
        try {
            File file = new File(path);
            Scanner scanner;
            try {
                scanner = new Scanner(file);
            } catch (FileNotFoundException e) {
                throw new Exception("Archivo no encontrado: " + path, e);
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

            for (String line : lineasTitulares) {
                try {
                    procesarLinea(line, grupoPorTitular, true);
                } catch (Exception e) {
                    System.err.println("Error procesando titular: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            for (String line : lineasFamiliares) {
                try {
                    procesarLinea(line, grupoPorTitular, false);
                } catch (Exception e) {
                    System.err.println("Error procesando familiar: " + e.getMessage());
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            // Manejo global de errores
            System.err.println("Error global al importar Excel: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Falló la importación de beneficiarios", e);
        }
    }


    private void procesarLinea(String line, Map<Long, GrupoFamiliar> grupoPorTitular, boolean esTitular) throws Exception {
        StringTokenizer st = new StringTokenizer(line, "|");

        String rnos = st.nextToken();
        String cuitEmpresa = st.nextToken();
        String cuilTitular = st.nextToken();
        String tipoParentescoStr = st.nextToken();
        String cuilFamiliarStr = st.nextToken(); // Cuil Familiar
        st.nextToken();
        String dni = st.nextToken(); // documento
        String nombreCompleto = st.nextToken();
        String sexoStr = st.nextToken();
        String estadoCivilStr = st.nextToken();
        String fechaNacimientoStr = st.nextToken();
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
            case "00" -> tipoParentesco = TipoParentesco.Titular;
            case "01" -> tipoParentesco = TipoParentesco.Conyuge;
            case "02" -> tipoParentesco = TipoParentesco.Concubino_Concubina;
            case "03" -> tipoParentesco = TipoParentesco.Hijo_Soltero_Menor_De_21;
            case "04" -> tipoParentesco = TipoParentesco.Hijo_Soltero_Entre_21_25_Estudiando;
            case "05" -> tipoParentesco = TipoParentesco.Hijo_Conyuge_Menor_De_21;
            case "06" -> tipoParentesco = TipoParentesco.Hijo_Conyuge_Entre_21_25_Estudiando;
            case "07" -> tipoParentesco = TipoParentesco.Menor_Bajo_Guarda_Tutela;
            case "08" -> tipoParentesco = TipoParentesco.Familiar_A_Cargo;
            case "09" -> tipoParentesco = TipoParentesco.Mayor_de_25_Discapacitado;
        }



        // --- Provincia ---
        Provincia provincia = null;
        System.out.println(nombreCompleto);

        if (provincia1 != null && !provincia1.isBlank()) {
            try {
                Long provinciaId = Long.parseLong(provincia1.trim());

                // Si no es 99, le sumamos 1
                if (provinciaId != 99) {
                    provinciaId += 1;
                }

                // Buscamos o creamos el país Argentina
                Pais paisArgentina = paisRepository.findByNombre("ARGENTINA")
                        .orElseGet(() -> paisRepository.save(
                                Pais.builder()
                                        .nombre("ARGENTINA")
                                        .activo(true)
                                        .build()
                        ));

                provincia = provinciaService.ListarPorId(provinciaId)
                        .orElse(Provincia.builder()
                                .nombre("Provincia Desconocida") // nombre por defecto si no existe
                                .pais(paisArgentina)
                                .activo(true)
                                .build());

                provinciaRepository.save(provincia);

            } catch (NumberFormatException e) {
                // Manejo de error si provincia1 no es un número válido
                Pais paisArgentina = paisRepository.findByNombre("Argentina")
                        .orElseGet(() -> paisRepository.save(
                                Pais.builder()
                                        .nombre("Argentina")
                                        .activo(true)
                                        .build()
                        ));

                provincia = Provincia.builder()
                        .nombre("Provincia Desconocida")
                        .pais(paisArgentina)
                        .activo(true)
                        .build();

                provinciaRepository.save(provincia);
            }
        }




        // --- Departamento ---
        Departamento departamento = null;
        if (departamento1 != null && !departamento1.isBlank()) {
            try {
                departamento = departamentoService.ListarPorNombre(departamento1);
            } catch (Exception e) {
                departamento = Departamento.builder()
                        .nombre(departamento1)
                        .activo(true)
                        .provincia(provincia)
                        .build();
                departamentoRepository.save(departamento);
            }
        }

// --- Localidad ---
        Localidad localidad = null;
        if (localidadNombre != null && !localidadNombre.isBlank() && departamento != null) {
            try {
                localidad = localidadService.listarPorDepartamentoYNombre(localidadNombre, departamento.getId())
                        .orElseThrow(() -> new Exception("No se encontró la localidad"));
            } catch (Exception e) {
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
        Domicilio domicilio = null;
        if (calle != null && !calle.isBlank()) {
            try {
                // Intentamos buscar el domicilio por calle, numeración y departamento/localidad
                Optional<Domicilio> optionalDomicilio = domicilioRepository
                        .findByCalleAndNumeracionAndLocalidad_Id(calle, puerta, (localidad != null ? localidad.getId() : null));

                domicilio = optionalDomicilio.orElseThrow(() -> new Exception("No se encontró el domicilio"));
            } catch (Exception e) {
                // Si no existe, lo creamos
                domicilio = Domicilio.builder()
                        .calle(calle)
                        .numeracion(puerta)
                        .manzanaPiso(piso)
                        .casaDepartamento(departamento1)
                        .localidad(localidad)
                        .barrio("Indefinido")
                        .referencia("Indefinido")
                        .tipoDomicilio(tipoDomicilioStr != null && tipoDomicilioStr.equalsIgnoreCase("DOMICILIO_RURAL")
                                ? TipoDeDomicilio.DOMICILIO_RURAL
                                : TipoDeDomicilio.DOMICILIO_COMPLETO)
                        .activo(true)
                        .build();
                domicilioRepository.save(domicilio);
            }
        }


        // --- Empresa ---
        Empresa empresa = null;
        if (cuitEmpresa != null && !cuitEmpresa.isBlank()) {
            try {
                empresa = empresaService.buscarPorCuit2(cuitEmpresa);
            } catch (Exception e) {
                // Si no existe, crearla y setearle domicilio
                empresa = Empresa.builder()
                        .cuit(cuitEmpresa.trim())
                        .razonSocial("Indefinida")
                        .activo(true)
                        .beneficiarios(new HashSet<>())
                        .domicilio(domicilio) // se asegura que tenga uno siempre
                        .build();
                empresaRepository.save(empresa);
            }
        } else {
            // Si no viene cuit, igualmente crear empresa genérica
            empresa = Empresa.builder()
                    .cuit("00000000000")
                    .razonSocial("Indefinida")
                    .activo(true)
                    .beneficiarios(new HashSet<>())
                    .domicilio(domicilio)
                    .build();
            empresaRepository.save(empresa);
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
            } catch (NumberFormatException ignored) {
            }
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
            } catch (Exception ignored) {
            }
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



        if (tipoParentesco == TipoParentesco.Titular) {
            // Buscar beneficiario existente por CUIL
            //Optional<Beneficiario> optionalTitular = beneficiarioService.ListarPorCuil(Long.parseLong(cuilTitular));

            EstadoCivil estadoCivil = EstadoCivil.Sin_Informacion;
            if (estadoCivilStr != null && !estadoCivilStr.isBlank()) {
                switch (estadoCivilStr.trim()) {
                    case "01" -> estadoCivil = EstadoCivil.Soltero;
                    case "02" -> estadoCivil = EstadoCivil.Casado;
                    case "03" -> estadoCivil = EstadoCivil.Viudo;
                    case "04" -> estadoCivil = EstadoCivil.Separado_Legal;
                    case "05" -> estadoCivil = EstadoCivil.Separado_De_Hecho;
                    case "06" -> estadoCivil = EstadoCivil.Divorciado;
                    case "07" -> estadoCivil = EstadoCivil.Convivencia;
                    default -> estadoCivil = EstadoCivil.Sin_Informacion;
                }
            }
            // --- Buscar el rol USER ya existente ---
            Rol rolUser = rolRepository.findByNombre("USER")
                    .orElseThrow(() -> new RuntimeException("No se encontró el rol USER en la base de datos"));

            // --- Crear usuario asociado ---
            Usuario user = Usuario.builder()
                    .email(dni+ "@mail.com") // el email será el cuil del titular
                    .contrasena(cuilTitular)    // contraseña temporal = dni
                    .rol(rolUser)
                    .activo(true)
                    .build();
            usuarioRepository.save(user);


            Beneficiario titular;
            titular = Beneficiario.builder()
                    .nombre(nombre)
                    .apellido(apellido)
                    .cuil(Long.parseLong(cuilTitular))
                    .telefono(telefono)
                    .sexo(sexo)
                    .empresa(empresa)
                    .afiliadoSindical(false)
                    .esJubilado(tipoBeneficiarioStr != null && tipoBeneficiarioStr.trim().equals("02"))
                    .estadoCivil(estadoCivil)
                    .fechaNacimiento(fechaNacimiento)
                    .nacionalidad(nacionalidad)
                    .domicilio(domicilio)
                    .usuario(user)
                    .build();

            beneficiarioRepository.save(titular);


            Date fechaAlta = new Date();
            if (fechaAltaOSStr != null && !fechaAltaOSStr.isBlank()) {
                try {
                    fechaAlta = new SimpleDateFormat("ddMMyyyy").parse(fechaAltaOSStr);
                } catch (Exception ignored) {}
            }

            GrupoFamiliar grupo;
                    grupo = GrupoFamiliar.builder()
                            .titular(titular)
                            .tipoBeneficiarioTitular(TipoDeBeneficiarioTitular.SIN_INFORMACION)
                            .nombreGrupo(titular.getNombre() + " " + titular.getApellido())
                            .activo(true)
                            .fechaAlta(fechaAlta)
                            .familiares(new ArrayList<>())
                            .build();
            grupoFamiliarRepository.save(grupo);
            grupoPorTitular.put(titular.getCuil(), grupo);

        } else {
            // --- Familiar ---
            GrupoFamiliar grupo = grupoPorTitular.get(Long.parseLong(cuilTitular));
            if (grupo == null) throw new Exception("No se encontró grupo familiar para CUIL titular: " + cuilTitular);

            Familiar familiar = Familiar.builder()
                    .nombre(nombre)
                    .apellido(apellido)
                    .cuil(Long.parseLong(cuilFamiliarStr))
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
