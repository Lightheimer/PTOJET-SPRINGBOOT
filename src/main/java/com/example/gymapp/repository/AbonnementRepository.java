package com.example.gymapp.repository;

import com.example.gymapp.model.Abonnement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface AbonnementRepository extends JpaRepository<Abonnement, Long> {
    
    @Query("SELECT COALESCE(SUM(p.prixMensuel), 0) " +
           "FROM Abonnement a JOIN a.pack p " +
           "WHERE MONTH(a.dateDebut) = MONTH(CURRENT_DATE) " +
           "AND YEAR(a.dateDebut) = YEAR(CURRENT_DATE)")
    double getTotalRevenueForCurrentMonth();
}
