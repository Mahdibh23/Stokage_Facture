package com.example.SpringBoot.service;

import com.example.SpringBoot.model.Entreprise;
import com.example.SpringBoot.repository.EntrepriseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EntrepriseService {

    @Autowired
    private EntrepriseRepository entrepriseRepository;

    public List<Entreprise> getAllEntreprises() {
        return entrepriseRepository.findAll();
    }

    public Entreprise getEntrepriseById(Long id) {
        return entrepriseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entreprise not found with id " + id));
    }

    public Entreprise createEntreprise(Entreprise entreprise) {
        return entrepriseRepository.save(entreprise);
    }

    public Entreprise updateEntreprise(Long id, Entreprise entrepriseUpdated) {
        return entrepriseRepository.findById(id)
                .map(entreprise -> {
                    entreprise.setName(entrepriseUpdated.getName());
                    entreprise.setDescription(entrepriseUpdated.getDescription());
                    entreprise.setAdresse(entrepriseUpdated.getAdresse());
                    entreprise.setCode(entrepriseUpdated.getCode());
                    entreprise.setTotalAmount(entrepriseUpdated.getTotalAmount());
                    return entrepriseRepository.save(entreprise);
                })
                .orElseThrow(() -> new RuntimeException("Entreprise not found with id " + id));
    }

    public void deleteEntreprise(Long id) {
        entrepriseRepository.deleteById(id);
    }
    // Méthode pour désactiver une entreprise
    public Entreprise desactiverEntreprise(Long id) {
        Entreprise entreprise = entrepriseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entreprise not found with id " + id));
        entreprise.setDisabled(true); // Assurez-vous que le nom de la méthode est correct
        return entrepriseRepository.save(entreprise);
    }

    // Méthode pour obtenir les entreprises désactivées
    public List<Entreprise> getEntreprisesDesactivees() {
        return entrepriseRepository.findByIsDisabledTrue();
    }

}
