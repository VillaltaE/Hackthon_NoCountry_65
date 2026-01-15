package com.hackathon.churninsight.repository;

import com.hackathon.churninsight.domain.PredictionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictionHistoryRepository extends JpaRepository<PredictionHistory, Long> {

    List<PredictionHistory> findTop20ByOrderByCreatedAtDesc();

    List<PredictionHistory> findTop20ByCustomerIdOrderByCreatedAtDesc(String customerId);
}
