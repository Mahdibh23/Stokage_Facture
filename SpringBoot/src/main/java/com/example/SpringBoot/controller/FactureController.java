package com.example.SpringBoot.controller;

import com.example.SpringBoot.model.Facture;
import com.example.SpringBoot.model.Entreprise;
import com.example.SpringBoot.service.FactureService;
import com.example.SpringBoot.service.EntrepriseService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/factures")
public class FactureController {
    private static final Logger logger = LoggerFactory.getLogger(FactureController.class);
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    @Autowired
    private FactureService factureService;

    @Autowired
    private EntrepriseService entrepriseService;

    @GetMapping
    public ResponseEntity<List<Facture>> getAllFactures() {
        try {
            List<Facture> factures = factureService.getAllFactures();
            return ResponseEntity.ok(factures);
        } catch (Exception e) {
            logger.error("Error fetching factures: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/")
    public ResponseEntity<Map<String, Object>> uploadFacture(
            @RequestParam("file") MultipartFile file,
            @RequestParam("invoice_number") String invoiceNumber,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("objet") String objet,
            @RequestParam("entreprise_id") Long entrepriseId) {

        Map<String, Object> response = new HashMap<>();

        try {
            Entreprise entreprise = entrepriseService.getEntrepriseById(entrepriseId);
            if (entreprise == null) {
                logger.error("Invalid entreprise ID: {}", entrepriseId);
                response.put("message", "Invalid entreprise ID.");
                return ResponseEntity.badRequest().body(response);
            }

            if (file.isEmpty()) {
                logger.error("No file selected for upload.");
                response.put("message", "Please select a file to upload.");
                return ResponseEntity.badRequest().body(response);
            }

            // Décompresser le fichier
            File decompressedFile = decompressImage(file);
            File uploadDir = new File("uploads");

            if (!uploadDir.exists()) {
                uploadDir.mkdirs();  // Crée le répertoire s'il n'existe pas
            }
            // Enregistrer le fichier décompressé dans le répertoire local
            String fileName = decompressedFile.getName();
            String filePath = Paths.get(UPLOAD_DIR, fileName).toString();

            filePath = filePath.replace("\\", "/");
            Files.copy(decompressedFile.toPath(), Paths.get(filePath));

            // Enregistrer le chemin du fichier dans la base de données
            Facture facture = new Facture(invoiceNumber, date, amount, objet, filePath, entreprise);
            Facture savedFacture = factureService.saveFacture(facture);

            response.put("message", "File uploaded successfully.");
            response.put("facture", savedFacture);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IOException e) {
            logger.error("Error saving file: {}", e.getMessage(), e);
            response.put("message", "Error saving file.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            logger.error("Error creating facture: {}", e.getMessage(), e);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Facture> getFactureById(@PathVariable Long id) {
        try {
            Facture facture = factureService.getFactureById(id);
            if (facture == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(facture);
        } catch (Exception e) {
            logger.error("Error fetching facture by ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Facture> updateFacture(@PathVariable Long id, @RequestBody Facture factureDetails) {
        try {
            Facture updatedFacture = factureService.updateFacture(id, factureDetails);
            if (updatedFacture == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedFacture);
        } catch (Exception e) {
            logger.error("Error updating facture: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacture(@PathVariable Long id) {
        try {
            factureService.deleteFacture(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting facture: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/entreprises/{entrepriseId}")
    public ResponseEntity<List<Facture>> getFacturesByEntrepriseId(@PathVariable Long entrepriseId) {
        try {
            List<Facture> factures = factureService.getFacturesByEntrepriseId(entrepriseId);
            return ResponseEntity.ok(factures);
        } catch (Exception e) {
            logger.error("Error fetching factures for entreprise ID {}: {}", entrepriseId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private File decompressImage(MultipartFile file) throws IOException {
        // Lire l'image compressée
        InputStream inputStream = new ByteArrayInputStream(file.getBytes());
        BufferedImage compressedImage = ImageIO.read(inputStream);

        // Créer un fichier temporaire pour l'image décompressée
        File tempFile = File.createTempFile("decompressed_", ".jpg");
        tempFile.deleteOnExit();

        // Écrire l'image décompressée dans le fichier temporaire
        try (OutputStream outputStream = new FileOutputStream(tempFile)) {
            ImageIO.write(compressedImage, "jpg", outputStream);
        }

        return tempFile;
    }
}
