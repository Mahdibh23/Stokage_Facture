package com.example.SpringBoot.service;

import com.example.SpringBoot.model.Facture;
import com.example.SpringBoot.model.Recu;
import com.example.SpringBoot.repository.RecuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecuService {

    @Autowired
    private RecuRepository recuRepository;

    public List<Recu> getAllRecus() {
        return recuRepository.findAll();
    }

    public Optional<Recu> getRecuById(Long id) {
        return recuRepository.findById(id);
    }

    public Recu createRecu(Recu recu) {
        return recuRepository.save(recu);
    }

    public Recu updateRecu(Long id, Recu recuDetails) {
        Recu recu = recuRepository.findById(id).orElseThrow(() -> new RuntimeException("Recu not found"));
        recu.setDate(recuDetails.getDate());
        recu.setAmount(recuDetails.getAmount());
        recu.setEntreprise(recuDetails.getEntreprise());
        return recuRepository.save(recu);
    }

    public void deleteRecu(Long id) {
        Recu recu = recuRepository.findById(id).orElseThrow(() -> new RuntimeException("Recu not found"));
        recuRepository.delete(recu);
    }
    public List<Recu> getRecusByEntrepriseId(Long entrepriseId) {
        return recuRepository.findByEntrepriseId(entrepriseId);
    }
}
