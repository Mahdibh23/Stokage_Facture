package com.example.SpringBoot.repository;

import com.example.SpringBoot.model.Facture;
import com.example.SpringBoot.model.Recu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecuRepository extends JpaRepository<Recu, Long> {
    List<Recu> findByEntrepriseId(Long entrepriseId);
}
