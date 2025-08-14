package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Documento;
import com.Ospuaye.BackendOspuaye.Repository.DocumentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class DocumentoService extends BaseService<Documento, Long> {

    public DocumentoService(DocumentoRepository repository) {
        super(repository);
    }

    /**
     * Guarda el archivo físicamente y devuelve la ruta absoluta (o nombre nuevo),
     * lanza Exception en caso de error para que el controller devuelva 400.
     */
    public String handleFileUpload(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new Exception("Archivo vacío");
        }

        String fileOriginalName = file.getOriginalFilename();
        if (fileOriginalName == null || fileOriginalName.isBlank()) {
            throw new Exception("Nombre de archivo inválido");
        }

        long size = file.getSize();
        long maxSize = 5L * 1024 * 1024; // 5 MB
        if (size > maxSize) {
            throw new Exception("El tamaño del archivo debe ser de 5MB o menor");
        }

        String lower = fileOriginalName.toLowerCase();
        if (!(lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png")
                || lower.endsWith(".pdf") || lower.endsWith(".doc") || lower.endsWith(".docx"))) {
            throw new Exception("Solo se aceptan archivos JPG, JPEG, PNG, PDF, DOC, DOCX");
        }

        String uuid = UUID.randomUUID().toString();
        String extension = fileOriginalName.substring(fileOriginalName.lastIndexOf("."));
        String newFileName = uuid + extension;
        File folder = new File("C://Ospuaye/documentos");
        if (!folder.exists() && !folder.mkdirs()) {
            throw new Exception("No se pudo crear la carpeta de destino");
        }

        Path path = Paths.get(folder.getAbsolutePath(), newFileName);
        Files.write(path, file.getBytes());

        return path.toString(); // o newFileName si preferís guardar sólo el nombre
    }
}
