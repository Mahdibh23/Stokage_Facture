package com.example.SpringBoot.repository;

import com.example.SpringBoot.model.Entreprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntrepriseRepository extends JpaRepository<Entreprise, Long> {
    List<Entreprise> findByIsDisabledTrue();
}
