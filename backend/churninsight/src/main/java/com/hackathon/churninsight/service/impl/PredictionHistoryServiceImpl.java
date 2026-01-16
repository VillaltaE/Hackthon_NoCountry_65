package com.hackathon.churninsight.service.impl;

import com.hackathon.churninsight.domain.PredictionHistory;
import com.hackathon.churninsight.dto.response.PredictionHistoryDTO;
import com.hackathon.churninsight.repository.PredictionHistoryRepository;
import com.hackathon.churninsight.service.PredictionHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PredictionHistoryServiceImpl implements PredictionHistoryService {

    private final PredictionHistoryRepository repo;

    public PredictionHistoryServiceImpl(PredictionHistoryRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<PredictionHistoryDTO> latest() {
        return repo.findTop20ByOrderByCreatedAtDesc().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<PredictionHistoryDTO> byCustomer(String customerId) {
        return repo.findTop20ByCustomerIdOrderByCreatedAtDesc(customerId).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public void clearAll() {
        repo.deleteAll();
    }

    private PredictionHistoryDTO toDto(PredictionHistory h) {
        return new PredictionHistoryDTO(
                h.getId(),
                h.getCustomerId(),
                h.getSubscriptionType(),
                h.getPaymentMethod(),
                h.getMonthlyFee(),
                h.getWatchHours(),
                h.getLastLoginDays(),
                h.getNumberOfProfiles(),
                h.getAvgWatchTimePerDay(),
                h.getProbability(),
                h.getLabel(),
                h.getPredictionLabel(),
                h.getCreatedAt()
        );
    }
}
