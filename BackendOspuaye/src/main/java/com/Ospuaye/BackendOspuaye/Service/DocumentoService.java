package com.Ospuaye.BackendOspuaye.Service;

import com.Ospuaye.BackendOspuaye.Entity.Documento;
import com.Ospuaye.BackendOspuaye.Repository.DocumentoRepository;
import org.aspectj.apache.bcel.classfile.Module;
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

    public String handleFileUpload(MultipartFile file) throws Exception {
        try{
            String fileName = UUID.randomUUID().toString();
            byte[] bytes = file.getBytes();
            String fileOriginalName = file.getOriginalFilename();

            //tamaño maximo de archivos
            long size = file.getSize();
            long maxSize = 5 * 1024 * 1024;

            if (size > maxSize) {
                return("El tamaño del archivo debe ser de 5MB o menor");
            }

            if (
                    !fileOriginalName.endsWith(".jpg") &&
                    !fileOriginalName.endsWith(".jpeg") &&
                    !fileOriginalName.endsWith(".png") &&
                    !fileOriginalName.endsWith(".pdf") &&
                    !fileOriginalName.endsWith(".dox")

            ){
                return "Solo se aceptan archivos en formato JPG, JPEG, PNG, PDF, DOX";
            }

            String fileExtension = fileOriginalName.substring(fileOriginalName.lastIndexOf("."));
            String newFileName = fileName + fileExtension;

            File folder = new File("backendPasantias/BackendOspuaye/src/main/resources/pictures");
            if(!folder.exists()){
                folder.mkdirs();
            }

            Path path = Paths.get("backendPasantias/BackendOspuaye/src/main/resources/pictures/" + newFileName);
            Files.write(path, bytes);
            return "Archivo cargado correctamente";

        }catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


}
