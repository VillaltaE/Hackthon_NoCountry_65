package com.hackathon.churninsight.repository;

import com.hackathon.churninsight.domain.PredictionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface PredictionHistoryRepository extends JpaRepository<PredictionHistory, Long> {

    // Método para obtener todas las predicciones con paginación
    Page<PredictionHistory> findAll(Pageable pageable);

    // Método para obtener las predicciones de un cliente específico con paginación
    Page<PredictionHistory> findByCustomerId(String customerId, Pageable pageable);

    // Método para filtrar predicciones por rango de fechas
    Page<PredictionHistory> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
