package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.*;
import com.Ospuaye.BackendOspuaye.Entity.Enum.*;
import com.Ospuaye.BackendOspuaye.Repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
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
    private final FamiliarRepository familiarRepository;
    private final DomicilioService domicilioService;
    private final PasswordEncoder passwordEncoder;


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
                                     DepartamentoRepository departamentoRepository, RolRepository rolRepository, UsuarioRepository usuarioRepository, PaisRepository paisRepository, FamiliarRepository familiarRepository, DomicilioService domicilioService, PasswordEncoder passwordEncoder) {
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
        this.familiarRepository = familiarRepository;
        this.domicilioService = domicilioService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
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
            System.err.println("Error global al importar Excel: " + e.getClass().getName() + " - " + e.getMessage());
            e.printStackTrace();
            throw e;
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


// --- Localidad ---
        Localidad localidad = null;
        System.out.println("Ejecuntando Localidades");
        if (localidadNombre != null && !localidadNombre.isBlank()) {
            try {
                localidad = localidadService.listarPorNombre(localidadNombre)
                        .orElseThrow(() -> new Exception("No se encontró la localidad"));
            } catch (Exception e) {
                localidad = Localidad.builder()
                        .nombre(localidadNombre)
                        .codigoPostal(codigoPostal)
                        .departamento(null)
                        .activo(true)
                        .build();
                localidadRepository.save(localidad);
            }
        }


        // --- Domicilio ---
        Domicilio domicilio = null;
        System.out.println("Ejecuntando Domicilios");
        if (calle != null && !calle.isBlank()) {
            // Intentamos buscar el domicilio por calle, numeración y departamento/localidad
            Optional<Domicilio> optionalDomicilio = domicilioService.listarPorCalleYNumeracionYLocalidad(calle, puerta, localidad.getId());

            if (optionalDomicilio.isPresent()) {
                domicilio = optionalDomicilio.get();
            } else {
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
                domicilio = domicilioRepository.save(domicilio);
            }
        }
        if (domicilio != null && domicilio.getId() != null) {
            domicilio = domicilioRepository.findById(domicilio.getId()).orElse(domicilio);
        }


        // --- Empresa ---
        Empresa empresa = null;
        System.out.println("Ejecuntando Empresas");
        if (cuitEmpresa != null && !cuitEmpresa.isBlank()) {
            Optional<Empresa> optionalEmpresa = empresaService.buscarPorCuit2(cuitEmpresa);

            if (optionalEmpresa.isPresent()) {
                empresa = optionalEmpresa.get();
            } else {
                // Si no existe, crearla
                empresa = Empresa.builder()
                        .cuit(cuitEmpresa.trim())
                        .razonSocial("Indefinida")
                        .activo(true)
                        .beneficiarios(new HashSet<>())
                        .domicilio(domicilio)
                        .build();
                empresa = empresaRepository.save(empresa);
            }
        } else {
            // Si no viene cuit, crear empresa genérica
            empresa = Empresa.builder()
                    .cuit("00000000000")
                    .razonSocial("Indefinida")
                    .activo(true)
                    .beneficiarios(new HashSet<>())
                    .domicilio(domicilio)
                    .build();
            empresa = empresaRepository.save(empresa);
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
        System.out.println("Ejecuntando Nacionalidad");
        if (nacionalidadNombre != null && !nacionalidadNombre.isBlank()) {
            nacionalidad = nacionalidadService.ListarPorId(Long.parseLong(nacionalidadNombre.trim()))
                    .orElse(Nacionalidad.builder()
                            .nombre(nacionalidadNombre)
                            .activo(true)
                            .build());
            nacionalidadRepository.save(nacionalidad);
        }



        if (tipoParentesco == TipoParentesco.Titular) {

            // --- ESTADO CIVIL ---
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

            // --- INCAPACIDAD ---
            Incapacidad incapacidad = Incapacidad.SIN_INFORMACION;
            if (incapacidadStr != null && !incapacidadStr.isBlank()) {
                switch (incapacidadStr.trim()) {
                    case "00" -> incapacidad = Incapacidad.NO_INCAPACITADO;
                    case "01" -> incapacidad = Incapacidad.INCAPACITADO;
                    default -> incapacidad = Incapacidad.SIN_INFORMACION;
                }
            }

            // --- ROL USER ---
            Rol rolUser = rolRepository.findByNombre("USER").orElse(null);
            if (rolUser == null) {
                throw new RuntimeException("No se encontró el rol USER en la base de datos");
            }

            // --- VALIDACIONES PREVIAS ---
            if (dni == null || dni.isBlank()) {
                throw new IllegalArgumentException("El DNI no puede ser nulo o vacío");
            }
            if (cuilTitular == null || cuilTitular.isBlank()) {
                throw new IllegalArgumentException("El CUIL no puede ser nulo o vacío");
            }

            String emailGenerado = dni + "@mail.com";

            // --- USUARIO ---
            System.out.println("Ejecuntando Usuario");
            Usuario userExistente = usuarioRepository.findByEmail(emailGenerado).orElse(null);
            Usuario user;
            if (userExistente != null) {
                user = userExistente;
            } else {
                user = Usuario.builder()
                        .email(emailGenerado)
                        .contrasena(passwordEncoder.encode(cuilTitular))
                        .rol(rolUser)
                        .activo(true)
                        .build();
                user = usuarioRepository.save(user);
            }

            // --- BENEFICIARIO ---
            System.out.println("Ejecuntando Beneficiario");
            Long cuilLong;
            try {
                cuilLong = Long.parseLong(cuilTitular);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("El CUIL del beneficiario debe ser numérico: " + cuilTitular);
            }

            Beneficiario titular;
            Beneficiario titularExistente = beneficiarioRepository.findByCuil(cuilLong).orElse(null);
            if (titularExistente != null) {
                titular = titularExistente;
                System.out.println("✅ Titular ya existente con CUIL: " + cuilLong + ", no se vuelve a guardar."); // ✅ NUEVO
            } else {
                // ✅ NUEVO: verifico también por DNI por si no tiene CUIL cargado pero sí DNI
                Beneficiario titularPorDni = beneficiarioRepository.findByDni(Long.parseLong(dni)).orElse(null);
                if (titularPorDni != null) {
                    titular = titularPorDni;
                    System.out.println("✅ Titular ya existente con DNI: " + dni + ", no se vuelve a guardar."); // ✅ NUEVO
                } else {
                    titular = Beneficiario.builder()
                            .nombre(nombre)
                            .apellido(apellido)
                            .cuil(cuilLong)
                            .dni(Long.parseLong(dni))
                            .telefono(telefono)
                            .sexo(sexo)
                            .empresa(empresa)
                            .afiliadoSindical(true)
                            .esJubilado(tipoBeneficiarioStr != null && tipoBeneficiarioStr.trim().equals("02"))
                            .estadoCivil(estadoCivil)
                            .fechaNacimiento(fechaNacimiento)
                            .nacionalidad(nacionalidad)
                            .domicilio(domicilio)
                            .activo(true)
                            .incapacidad(incapacidad)
                            .usuario(user)
                            .build();
                    titular = beneficiarioRepository.save(titular);
                    System.out.println("🆕 Titular nuevo guardado con CUIL: " + cuilLong);
                }
            }

            // --- FECHA DE ALTA ---
            Date fechaAlta = new Date();
            if (fechaAltaOSStr != null && !fechaAltaOSStr.isBlank()) {
                try {
                    fechaAlta = new SimpleDateFormat("ddMMyyyy").parse(fechaAltaOSStr);
                } catch (Exception ignored) {}
            }

            System.out.println("Titular ID: " + titular.getId());
            if (!beneficiarioRepository.existsById(titular.getId())) {
                throw new RuntimeException("Titular no existe en DB");
            }

            // --- GRUPO FAMILIAR ---
            GrupoFamiliar grupo = null;
            Optional<GrupoFamiliar> grupoExistenteOpt = grupoFamiliarRepository.findByTitularId(titular.getId());
            if (grupoExistenteOpt.isPresent()) {
                grupo = grupoExistenteOpt.get();
                System.out.println("Grupo familiar ya existente para el titular: " + titular.getNombre() + " " + titular.getApellido());
            } else {
                grupo = GrupoFamiliar.builder()
                        .titular(titular)
                        .tipoBeneficiarioTitular(TipoDeBeneficiarioTitular.SIN_INFORMACION)
                        .nombreGrupo(titular.getNombre() + " " + titular.getApellido())
                        .activo(true)
                        .fechaAlta(fechaAlta)
                        .familiares(new ArrayList<>())
                        .build();
                grupoFamiliarRepository.save(grupo);
                System.out.println("Creado nuevo grupo familiar para el titular: " + titular.getNombre() + " " + titular.getApellido());
            }

            grupoPorTitular.put(titular.getCuil(), grupo);

        } else {
            // --- FAMILIAR ---
            GrupoFamiliar grupo = grupoPorTitular.get(Long.parseLong(cuilTitular));
            if (grupo == null) {
                throw new Exception("No se encontró grupo familiar para CUIL titular: " + cuilTitular);
            }

            try {
                Long cuilLong = Long.parseLong(cuilFamiliarStr);
                System.out.println("Ejecuntando Familiar");

                Familiar familiarExistente = familiarRepository.findByCuil(cuilLong).orElse(null);
                Familiar familiar;

                if (familiarExistente != null) {
                    familiar = familiarExistente;
                    System.out.println("✅ Familiar ya existente con CUIL: " + cuilLong + ", no se vuelve a guardar."); // ✅ NUEVO
                } else {
                    familiar = Familiar.builder()
                            .nombre(nombre)
                            .apellido(apellido)
                            .dni(Long.parseLong(dni))
                            .correoElectronico("Sin Definir")
                            .beneficiario(grupo.getTitular())
                            .cuil(cuilLong)
                            .fechaNacimiento(fechaNacimiento)
                            .domicilio(domicilio)
                            .telefono(telefono)
                            .sexo(sexo)
                            .nacionalidad(nacionalidad)
                            .grupoFamiliar(grupo)
                            .tipoParentesco(tipoParentesco)
                            .activo(true)
                            .build();
                    familiar = familiarRepository.save(familiar);
                    System.out.println("🆕 Familiar nuevo guardado con CUIL: " + cuilLong);
                }

                grupo.getFamiliares().add(familiar);
                grupoFamiliarRepository.save(grupo);

            } catch (Exception e) {
                System.err.println("⚠️ Error guardando familiar (" + nombre + " " + apellido + "): " + e.getMessage());
                e.printStackTrace();
            }
        }


    }
}
