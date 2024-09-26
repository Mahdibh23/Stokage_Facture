package com.example.SpringBoot.controller;

import com.example.SpringBoot.model.Entreprise;
import com.example.SpringBoot.service.EntrepriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entreprises")
public class EntrepriseController {

    @Autowired
    private EntrepriseService entrepriseService;

    // Endpoint pour récupérer toutes les entreprises
    @GetMapping("/")
    public List<Entreprise> getAllEntreprises() {
        return entrepriseService.getAllEntreprises();
    }

    // Endpoint pour récupérer une entreprise par son ID
    @GetMapping("/{id}")
    public Entreprise getEntrepriseById(@PathVariable Long id) {
        return entrepriseService.getEntrepriseById(id);
    }

    // Endpoint pour créer une nouvelle entreprise
    @PostMapping("/")
    public Entreprise createEntreprise(@RequestBody Entreprise entreprise) {
        return entrepriseService.createEntreprise(entreprise);
    }

    // Endpoint pour mettre à jour une entreprise existante
    @PutMapping("/{id}")
    public Entreprise updateEntreprise(@PathVariable Long id, @RequestBody Entreprise entrepriseUpdated) {
        return entrepriseService.updateEntreprise(id, entrepriseUpdated);
    }

    // Endpoint pour supprimer une entreprise
    @DeleteMapping("/{id}")
    public void deleteEntreprise(@PathVariable Long id) {
        entrepriseService.deleteEntreprise(id);
    }

    @PutMapping("/{id}/desactiver")
    public ResponseEntity<Entreprise> desactiverEntreprise(@PathVariable Long id) {
        Entreprise entreprise = entrepriseService.desactiverEntreprise(id);
        return ResponseEntity.ok(entreprise);
    }

    @GetMapping("/desactivees")
    public ResponseEntity<List<Entreprise>> getEntreprisesDesactivees() {
        List<Entreprise> entreprisesDesactivees = entrepriseService.getEntreprisesDesactivees();
        return ResponseEntity.ok(entreprisesDesactivees);
    }
}
