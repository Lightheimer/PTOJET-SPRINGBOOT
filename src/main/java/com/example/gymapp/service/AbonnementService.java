package com.example.gymapp.service;

import com.example.gymapp.model.Abonnement;
import com.example.gymapp.repository.AbonnementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbonnementService {
    private final AbonnementRepository abonnementRepository;

    public AbonnementService(AbonnementRepository abonnementRepository) {
        this.abonnementRepository = abonnementRepository;
    }

    public List<Abonnement> getAllAbonnements() {
        return abonnementRepository.findAll();
    }

    public Abonnement createAbonnement(Abonnement abonnement) {
        return abonnementRepository.save(abonnement);
    }

    public void deleteAbonnement(Long id) {
        abonnementRepository.deleteById(id);
    }
}
