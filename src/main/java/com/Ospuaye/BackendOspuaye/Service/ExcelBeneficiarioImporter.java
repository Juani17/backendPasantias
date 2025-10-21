package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.*;
import com.Ospuaye.BackendOspuaye.Entity.Enum.*;
import com.Ospuaye.BackendOspuaye.Repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ImportacionService {

    private final BeneficiarioRepository beneficiarioRepository;
    private final GrupoFamiliarRepository grupoFamiliarRepository;
    private final FamiliarRepository familiarRepository;
    private final EmpresaRepository empresaRepository;
    private final DomicilioRepository domicilioRepository;
    private final NacionalidadRepository nacionalidadRepository;

    public ImportacionService(BeneficiarioRepository beneficiarioRepository,
                              GrupoFamiliarRepository grupoFamiliarRepository,
                              FamiliarRepository familiarRepository,
                              EmpresaRepository empresaRepository,
                              DomicilioRepository domicilioRepository,
                              NacionalidadRepository nacionalidadRepository) {
        this.beneficiarioRepository = beneficiarioRepository;
        this.grupoFamiliarRepository = grupoFamiliarRepository;
        this.familiarRepository = familiarRepository;
        this.empresaRepository = empresaRepository;
        this.domicilioRepository = domicilioRepository;
        this.nacionalidadRepository = nacionalidadRepository;
    }

    @Transactional
    public void importarDesdeArchivo(String pathArchivo) throws Exception {
        Scanner sc;
        try {
            sc = new Scanner(new File(pathArchivo));
        } catch (FileNotFoundException e) {
            throw new Exception("Archivo no encontrado: " + pathArchivo);
        }

        // Omitimos encabezado
        if (sc.hasNextLine()) sc.nextLine();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        while (sc.hasNextLine()) {
            String linea = sc.nextLine();
            StringTokenizer st = new StringTokenizer(linea, "\t");

            String cuitEmpresa = st.nextToken();
            String cuilTitular = st.nextToken();
            String tipoParentescoStr = st.nextToken(); // solo para familiares
            String cuilFamiliar = st.nextToken();
            String tipoDocumentoStr = st.nextToken();
            String documento = st.nextToken();
            String nombreApellido = st.nextToken();
            String sexoStr = st.nextToken();
            String fechaNacimientoStr = st.nextToken();
            String edad = st.nextToken(); // podemos ignorar
            String nacionalidadStr = st.nextToken();
            String calle = st.nextToken();
            String puerta = st.nextToken();
            String piso = st.nextToken();
            String departamento = st.nextToken();
            String localidadNombre = st.nextToken();
            String codigoPostal = st.nextToken(); // podemos ignorar
            String provincia = st.nextToken(); // podemos ignorar
            String tipoDomicilioStr = st.nextToken();
            String telefonoStr = st.nextToken();
            String situacionRevista = st.nextToken(); // opcional
            String incapacidad = st.nextToken(); // opcional
            String tipoBeneficiarioStr = st.nextToken();
            String fechaAltaOS = st.nextToken(); // opcional
            String validaCuil = st.hasMoreTokens() ? st.nextToken() : null;
            String cuilObraSocial = st.hasMoreTokens() ? st.nextToken() : null;
            String tipoBeneficiarioInfOS = st.hasMoreTokens() ? st.nextToken() : null;
            String cuitEmpleadorOS = st.hasMoreTokens() ? st.nextToken() : null;

            // Empresa
            Empresa empresa = empresaRepository.findByCuit(cuitEmpresa)
                    .orElseGet(() -> {
                        Empresa e = new Empresa();
                        e.setCuit(cuitEmpresa);
                        e.setRazonSocial("Empresa " + cuitEmpresa);
                        e.setActivo(true);
                        return empresaRepository.save(e);
                    });

            // Nacionalidad
            Nacionalidad nacionalidad = nacionalidadRepository.findByNombre(nacionalidadStr)
                    .orElseGet(() -> {
                        Nacionalidad n = new Nacionalidad();
                        n.setNombre(nacionalidadStr);
                        n.setActivo(true);
                        return nacionalidadRepository.save(n);
                    });

            // Domicilio
            Domicilio domicilio = new Domicilio();
            domicilio.setCalle(calle);
            domicilio.setNumeracion(puerta);
            domicilio.setManzanaPiso(piso);
            domicilio.setCasaDepartamento(departamento);
            domicilio.setActivo(true);
            domicilio.setTipoDomicilio(TipoDeDomicilio.valueOf(tipoDomicilioStr.toUpperCase()));
            domicilioRepository.save(domicilio);

            // Beneficiario titular
            Beneficiario titular = beneficiarioRepository.findByCuil(Long.parseLong(cuilTitular))
                    .orElseGet(() -> {
                        Beneficiario b = new Beneficiario();
                        String[] partes = nombreApellido.split(" ", 2);
                        b.setNombre(partes[0]);
                        b.setApellido(partes.length > 1 ? partes[1] : "");
                        b.setCuil(Long.parseLong(cuilTitular));
                        b.setDni(Long.parseLong(documento));
                        b.setSexo(sexoStr.equalsIgnoreCase("M") ? Sexo.M : Sexo.F);
                        b.setTipoDocumento(TipoDocumento.valueOf(tipoDocumentoStr.toUpperCase()));
                        b.setNacionalidad(nacionalidad);
                        b.setDomicilio(domicilio);
                        b.setEmpresa(empresa);
                        return beneficiarioRepository.save(b);
                    });

            // Grupo familiar
            GrupoFamiliar grupo = grupoFamiliarRepository.findByTitularId(titular.getId())
                    .orElseGet(() -> {
                        GrupoFamiliar g = new GrupoFamiliar();
                        g.setTitular(titular);
                        g.setTipoBeneficiarioTitular(TipoDeBeneficiarioTitular.valueOf(tipoBeneficiarioStr.replaceAll("\\s+", "_").toUpperCase()));
                        g.setActivo(true);
                        g.setNombreGrupo("Grupo de " + titular.getNombre());
                        return grupoFamiliarRepository.save(g);
                    });

            // Familiar
            if (cuilFamiliar != null && !cuilFamiliar.isEmpty()) {
                Familiar familiar = new Familiar();
                String[] partesFam = nombreApellido.split(" ", 2);
                familiar.setNombre(partesFam[0]);
                familiar.setApellido(partesFam.length > 1 ? partesFam[1] : "");
                familiar.setCuil(Long.parseLong(cuilFamiliar));
                familiar.setTipoParentesco(TipoParentesco.valueOf(tipoParentescoStr.replaceAll("\\s+", "_").toUpperCase()));
                familiar.setGrupoFamiliar(grupo);
                familiar.setBeneficiario(titular);
                familiarRepository.save(familiar);
            }
        }

        sc.close();
    }
}
