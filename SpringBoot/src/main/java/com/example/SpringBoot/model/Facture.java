package com.example.SpringBoot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("invoice_number")
    private String invoiceNumber;
    private LocalDate date;
    private BigDecimal amount;
    private String objet;
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "entreprise_id")
    @JsonBackReference
    @NotNull
    private Entreprise entreprise;

    // Constructors
    public Facture() {
    }

    public Facture(String invoiceNumber, LocalDate date, BigDecimal amount, String objet, String filePath, Entreprise entreprise) {
        this.invoiceNumber = invoiceNumber;
        this.date = date;
        this.amount = amount;
        this.objet = objet;
        this.filePath = filePath;
        this.entreprise = entreprise;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
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

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    @Override
    public String toString() {
        return "Facture{" +
                "invoiceNumber='" + invoiceNumber + '\'' +
                ", date=" + date +
                ", amount=" + amount +
                ", objet='" + objet + '\'' +
                ", entreprise=" + (entreprise != null ? entreprise.getId() : "null") +
                '}';
    }
}
