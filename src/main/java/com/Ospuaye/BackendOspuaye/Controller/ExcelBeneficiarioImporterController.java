package com.Ospuaye.BackendOspuaye.Controller;

import com.Ospuaye.BackendOspuaye.Service.ExcelBeneficiarioImporter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/api/importar-beneficiarios")
@CrossOrigin(origins = "*")
public class ExcelBeneficiarioImporterController {

    private final ExcelBeneficiarioImporter excelBeneficiarioImporter;

    public ExcelBeneficiarioImporterController(ExcelBeneficiarioImporter excelBeneficiarioImporter) {
        this.excelBeneficiarioImporter = excelBeneficiarioImporter;
    }

    /**
     * Endpoint que permite subir un archivo .txt o .tsv (con tabulaciones)
     * y ejecuta el proceso de importación.
     */
    @PostMapping("/upload")
    public ResponseEntity<String> importarArchivo(@RequestParam("file") MultipartFile file) {
        try {
            // Guardar temporalmente el archivo subido
            File tempFile = File.createTempFile("beneficiarios-", ".txt");
            file.transferTo(tempFile);

            // Llamar al servicio de importación
            excelBeneficiarioImporter.importar(tempFile.getAbsolutePath());

            // Eliminar el archivo temporal
            tempFile.delete();

            return ResponseEntity.ok("Importación completada correctamente ✅");

        } catch (Exception e) {
            e.printStackTrace(); // sigue mostrando en consola

            // Devuelve información más útil al frontend
            String errorMsg = "Error durante la importación: " + e.getClass().getSimpleName() + " - " + e.getMessage();
            return ResponseEntity.badRequest().body(errorMsg);
        }
    }

    /**
     * Endpoint alternativo: importar indicando la ruta absoluta del archivo.
     */
    @PostMapping
    public ResponseEntity<String> importarPorRuta(@RequestParam("path") String path) {
        try {
            excelBeneficiarioImporter.importar(path);
            return ResponseEntity.ok("Importación completada desde ruta ✅");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al importar desde ruta: " + e.getMessage());
        }
    }
}
