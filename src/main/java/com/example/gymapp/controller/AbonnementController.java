package com.example.gymapp.controller;

import com.example.gymapp.model.Abonnement;
import com.example.gymapp.service.AbonnementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/abonnements")
public class AbonnementController {
    private final AbonnementService abonnementService;

    public AbonnementController(AbonnementService abonnementService) {
        this.abonnementService = abonnementService;
    }

    @GetMapping
    public List<Abonnement> getAllAbonnements() {
        return abonnementService.getAllAbonnements();
    }

    @PostMapping
    public Abonnement createAbonnement(@RequestBody Abonnement abonnement) {
        return abonnementService.createAbonnement(abonnement);
    }

    @DeleteMapping("/{id}")
    public void deleteAbonnement(@PathVariable Long id) {
        abonnementService.deleteAbonnement(id);
    }
}
