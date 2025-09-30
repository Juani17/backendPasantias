package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Documento;
import com.Ospuaye.BackendOspuaye.Entity.Pedido;
import com.Ospuaye.BackendOspuaye.Repository.DocumentoRepository;
import com.Ospuaye.BackendOspuaye.Repository.PedidoRepository;
import com.Ospuaye.BackendOspuaye.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

@Service
public class DocumentoService extends BaseService<Documento, Long> {

    private final DocumentoRepository documentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;

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

    // 🔹 MÉTODO NUEVO: Devuelve el nombre del archivo generado
    public String subirArchivo(MultipartFile file) throws Exception {
        try {
            if (file == null || file.isEmpty()) {
                throw new Exception("No se recibió archivo o está vacío");
            }

            String original = file.getOriginalFilename();
            if (original == null) {
                throw new Exception("El archivo no tiene nombre válido");
            }

            long size = file.getSize();
            long maxSize = 5 * 1024 * 1024; // 5MB
            if (size > maxSize) {
                throw new Exception("El tamaño del archivo debe ser de 5MB o menor");
            }

            String lower = original.toLowerCase();
            if (!(lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png")
                    || lower.endsWith(".pdf") || lower.endsWith(".doc") || lower.endsWith(".docx"))) {
                throw new Exception("Solo se aceptan archivos JPG, JPEG, PNG, PDF, DOC o DOCX");
            }

            // Generar nombre único para evitar conflictos
            String ext = original.substring(original.lastIndexOf("."));
            String newName = UUID.randomUUID() + ext;

            // Crear directorio si no existe
            File folder = new File("C://Ospuaye/documentos");
            if (!folder.exists()) {
                boolean created = folder.mkdirs();
                if (!created) {
                    throw new Exception("No se pudo crear el directorio de documentos");
                }
            }

            // Escribir archivo físico
            Path path = Paths.get(folder.getAbsolutePath(), newName);
            Files.write(path, file.getBytes());

            // 🔹 CAMBIO IMPORTANTE: Devolver el nombre real del archivo generado
            return newName;

        } catch (Exception e) {
            throw new Exception("Error al subir archivo: " + e.getMessage());
        }
    }

    // 🔹 Mantener método legacy para compatibilidad (DEPRECADO)
    @Deprecated
    public String handleFileUpload(MultipartFile file) throws Exception {
        try {
            subirArchivo(file);
            return "Archivo cargado correctamente";
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}