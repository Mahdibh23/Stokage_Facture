package com.example.SpringBoot.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
public class Entreprise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String adresse;
    private String code;
    private String email;
    private BigDecimal totalAmount;

    @Column(name = "is_disabled")
    private boolean isDisabled;

    @OneToMany(mappedBy = "entreprise")
    @JsonManagedReference
    private List<Facture> factures;

    @OneToMany(mappedBy = "entreprise")
    @JsonManagedReference
    private List<Recu> recu;

    public Entreprise() {
    }

    public Entreprise(String name, String description, String adresse, String code,String email) {
        this.name = name;
        this.description = description;
        this.adresse = adresse;
        this.code = code;
        this.totalAmount= BigDecimal.valueOf(0);
        this.email=email;
        this.isDisabled = false;
    }


    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Recu> getRecu() {
        return recu;
    }

    public void setRecu(List<Recu> recu) {
        this.recu = recu;
    }

    public List<Facture> getFactures() {
        return factures;
    }

    public void setFactures(List<Facture> factures) {
        this.factures = factures;
    }
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    // toString method
    @Override
    public String toString() {
        return "Entreprise{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", adresse='" + adresse + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
