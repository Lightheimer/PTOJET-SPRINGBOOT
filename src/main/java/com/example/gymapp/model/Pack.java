package com.example.gymapp.model;

import jakarta.persistence.*;

@Entity


public class Pack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomOffre;
    private int dureeMois;
    private double prixMensuel;

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomOffre() { return nomOffre; }
    public void setNomOffre(String nomOffre) { this.nomOffre = nomOffre; }
    public int getDureeMois() { return dureeMois; }
    public void setDureeMois(int dureeMois) { this.dureeMois = dureeMois; }
    public double getPrixMensuel() { return prixMensuel; }
    public void setPrixMensuel(double prixMensuel) { this.prixMensuel = prixMensuel; }

    }
