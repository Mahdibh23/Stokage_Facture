package com.example.SpringBoot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Recu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private BigDecimal amount;
    private String filePath;


    @ManyToOne
    @JoinColumn(name = "entreprise_id")
    @JsonBackReference
    private Entreprise entreprise;

    // Constructors
    public Recu() {}

    public Recu(LocalDate date, BigDecimal amount, Entreprise entreprise, String filePath) {
        this.date = date;
        this.amount = amount;
        this.entreprise = entreprise;
        this.filePath = filePath;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "Recu{" +
                "id=" + id +
                ", date=" + date +
                ", amount=" + amount +
                ", entreprise=" + (entreprise != null ? entreprise.getId() : "null") +
                '}';
    }
}
