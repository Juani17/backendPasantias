package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Documento;
import com.Ospuaye.BackendOspuaye.Entity.Pedido;
import com.Ospuaye.BackendOspuaye.Repository.DocumentoRepository;
import com.Ospuaye.BackendOspuaye.Repository.PedidoRepository;
import com.Ospuaye.BackendOspuaye.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentoService extends BaseService<Documento, Long> {

    private final DocumentoRepository documentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;

    // ✅ Inyectar ruta desde properties
    @Value("${app.documentos.directorio:/app/documentos}")
    private String directorioBase;

    public DocumentoService(DocumentoRepository repository,
                            UsuarioRepository usuarioRepository,
                            PedidoRepository pedidoRepository) {
        super(repository);
        this.documentoRepository = repository;
        this.usuarioRepository = usuarioRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public Documento crear(Documento d) throws Exception {
        validar(d);
        d.setFechaSubida(new Date());
        return documentoRepository.save(d);
    }

    @Override
    public Documento actualizar(Documento d) throws Exception {
        if (d.getId() == null || !documentoRepository.existsById(d.getId())) {
            throw new Exception("Documento no encontrado");
        }
        validar(d);
        return documentoRepository.save(d);
    }

    private void validar(Documento d) throws Exception {
        if (d.getNombreArchivo() == null || d.getNombreArchivo().isBlank()) {
            throw new Exception("El nombre de archivo es obligatorio");
        }
        if (d.getSubidoPor() == null || d.getSubidoPor().getId() == null
                || !usuarioRepository.existsById(d.getSubidoPor().getId())) {
            throw new Exception("El usuario que sube el documento no existe");
        }
        if (d.getPedido() != null) {
            Pedido p = d.getPedido();
            if (p.getId() == null || !pedidoRepository.existsById(p.getId())) {
                throw new Exception("El pedido asociado no existe");
            }
        }
    }

    /**
     * ✅ Upload físico multiplataforma
     * @param file archivo a subir
     * @return nombre único generado para el archivo
     * @throws Exception si hay error
     */
    public String handleFileUpload(MultipartFile file) throws Exception {
        try {
            if (file == null || file.isEmpty()) {
                throw new Exception("No se recibió archivo");
            }

            String original = file.getOriginalFilename();
            if (original == null) {
                throw new Exception("El archivo no tiene nombre válido");
            }

            // Validar tamaño (5MB)
            long size = file.getSize();
            long maxSize = 5 * 1024 * 1024;
            if (size > maxSize) {
                throw new Exception("El tamaño del archivo debe ser de 5MB o menor");
            }

            // Validar extensión
            String lower = original.toLowerCase();
            if (!(lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png")
                    || lower.endsWith(".pdf") || lower.endsWith(".doc") || lower.endsWith(".docx"))) {
                throw new Exception("Solo se aceptan archivos JPG, JPEG, PNG, PDF, DOC o DOCX");
            }

            // Generar nombre único
            String ext = original.substring(original.lastIndexOf("."));
            String newName = UUID.randomUUID() + ext;

            // ✅ Crear directorio usando Path (multiplataforma)
            Path directorioPath = Paths.get(directorioBase);
            if (!Files.exists(directorioPath)) {
                Files.createDirectories(directorioPath);
            }

            // ✅ Guardar archivo
            Path filePath = directorioPath.resolve(newName);
            Files.write(filePath, file.getBytes());

            // ✅ Retornar solo el nombre del archivo (no la ruta completa)
            return newName;

        } catch (Exception e) {
            throw new Exception("Error al subir archivo: " + e.getMessage());
        }
    }

    /**
     * ✅ Obtener el Path completo de un archivo
     */
    public Path obtenerRutaArchivo(String nombreArchivo) {
        return Paths.get(directorioBase).resolve(nombreArchivo);
    }

    /**
     * ✅ Verificar si un archivo existe
     */
    public boolean existeArchivo(String nombreArchivo) {
        return Files.exists(obtenerRutaArchivo(nombreArchivo));
    }

    public List<Documento> obtenerDocumentosPorPedido(Long pedidoId) throws Exception {
        if (pedidoId == null) {
            throw new Exception("El ID del pedido no puede ser nulo");
        }
        if (!pedidoRepository.existsById(pedidoId)) {
            throw new Exception("El pedido no existe");
        }
        return documentoRepository.findByPedidoId(pedidoId);
    }
}