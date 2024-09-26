package com.example.SpringBoot.service;

import com.example.SpringBoot.model.Facture;
import com.example.SpringBoot.repository.FactureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FactureService {

    @Autowired
    private FactureRepository factureRepository;

    public List<Facture> getAllFactures() {
        return factureRepository.findAll();
    }

    public Facture save(Facture facture) {
        return factureRepository.save(facture);
    }

    public Facture getFactureById(Long id) {
        return factureRepository.findById(id).orElseThrow(() -> new RuntimeException("Facture not found"));
    }

    public Facture saveFacture(Facture facture) {
        return factureRepository.save(facture);
    }

    public Facture updateFacture(Long id, Facture factureDetails) {
        Facture facture = factureRepository.findById(id).orElseThrow(() -> new RuntimeException("Facture not found"));

        facture.setInvoiceNumber(factureDetails.getInvoiceNumber());
        facture.setDate(factureDetails.getDate());
        facture.setAmount(factureDetails.getAmount());
        facture.setObjet(factureDetails.getObjet());
        facture.setEntreprise(factureDetails.getEntreprise());

        return factureRepository.save(facture);
    }

    public void deleteFacture(Long id) {
        Facture facture = factureRepository.findById(id).orElseThrow(() -> new RuntimeException("Facture not found"));
        factureRepository.delete(facture);
    }
    public List<Facture> getFacturesByEntrepriseId(Long entrepriseId) {
        return factureRepository.findByEntrepriseId(entrepriseId);
    }
}
