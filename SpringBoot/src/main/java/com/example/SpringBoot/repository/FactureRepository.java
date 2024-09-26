package com.example.SpringBoot.repository;

import com.example.SpringBoot.model.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {
    List<Facture> findByEntrepriseId(Long entrepriseId);
    List<Facture> findByDateBefore(LocalDate date);}