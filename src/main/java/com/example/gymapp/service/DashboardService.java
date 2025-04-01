package com.example.gymapp.service;

import com.example.gymapp.repository.AbonnementRepository;
import com.example.gymapp.repository.ClientRepository;
import com.example.gymapp.repository.PackRepository;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final AbonnementRepository abonnementRepository;
    private final ClientRepository clientRepository;
    private final PackRepository packRepository;

    public DashboardService(AbonnementRepository abonnementRepository, ClientRepository clientRepository, PackRepository packRepository) {
        this.abonnementRepository = abonnementRepository;
        this.clientRepository = clientRepository;
        this.packRepository = packRepository;
    }

    /**
     * 📊 Récupère les statistiques du Dashboard
     */
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        long totalClients = clientRepository.count();
        long totalAbonnements = abonnementRepository.count();
        long totalPacks = packRepository.count();
        double totalRevenue = abonnementRepository.getTotalRevenueForCurrentMonth(); // ✅ Utilisation de la requête corrigée

        stats.put("clients", totalClients);
        stats.put("abonnements", totalAbonnements);
        stats.put("packs", totalPacks);
        stats.put("revenu", totalRevenue);

        return stats;
    }
}
