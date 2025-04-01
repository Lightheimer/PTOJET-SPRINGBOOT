package com.example.gymapp.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity


public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private LocalDate dateInscription;
    private boolean abonnementActif;
    private String telephone;

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public LocalDate getDateInscription() { return dateInscription; }
    public void setDateInscription(LocalDate dateInscription) { this.dateInscription = dateInscription; }
    public boolean isAbonnementActif() { return abonnementActif; }
    public void setAbonnementActif(boolean abonnementActif) { this.abonnementActif = abonnementActif; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

}
