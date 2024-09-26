package com.example.SpringBoot.controller;

import com.example.SpringBoot.model.Entreprise;
import com.example.SpringBoot.model.Facture;
import com.example.SpringBoot.model.Recu;
import com.example.SpringBoot.service.EntrepriseService;
import com.example.SpringBoot.service.RecuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
import java.util.Optional;

@RestController
@RequestMapping("/api/recus")
@CrossOrigin(origins = "http://localhost:4200")

public class RecuController {
    private static final Logger logger = LoggerFactory.getLogger(RecuController.class);
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";
    @Autowired
    private RecuService recuService;

    @Autowired
    private EntrepriseService entrepriseService;

    @GetMapping
    public List<Recu> getAllRecus() {
        return recuService.getAllRecus();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recu> getRecuById(@PathVariable Long id) {
        Optional<Recu> recu = recuService.getRecuById(id);
        if (recu.isPresent()) {
            return ResponseEntity.ok(recu.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<Map<String, Object>> uploadRecu(
            @RequestParam("file") MultipartFile file,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("entreprise_id") Long entrepriseId) {

        Map<String, Object> response = new HashMap<>();

        try {
            Entreprise entreprise = entrepriseService.getEntrepriseById(entrepriseId);
            if (entreprise == null) {
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

            Recu recu = new Recu(date, amount, entreprise, filePath);
            Recu savedRecu = recuService.createRecu(recu);

            response.put("message", "File uploaded successfully.");
            response.put("recu", savedRecu);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recu> updateRecu(@PathVariable Long id, @RequestBody Recu recuDetails) {
        try {
            Recu updatedRecu = recuService.updateRecu(id, recuDetails);
            return ResponseEntity.ok(updatedRecu);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecu(@PathVariable Long id) {
        try {
            recuService.deleteRecu(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/entreprises/{entrepriseId}")
    public ResponseEntity<List<Recu>> getRecusByEntrepriseId(@PathVariable Long entrepriseId) {
        try {
            List<Recu> recus = recuService.getRecusByEntrepriseId(entrepriseId);
            return ResponseEntity.ok(recus);
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
