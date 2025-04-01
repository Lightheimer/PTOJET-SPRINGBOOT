package com.example.gymapp.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Abonnement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Pack pack;

    private LocalDate dateDebut;
    
    private LocalDate endDate; // ✅ Ajout de la date de fin

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public Pack getPack() { return pack; }
    public void setPack(Pack pack) { this.pack = pack; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getEndDate() { return endDate; } // ✅ Getter endDate
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; } // ✅ Setter endDate
}
