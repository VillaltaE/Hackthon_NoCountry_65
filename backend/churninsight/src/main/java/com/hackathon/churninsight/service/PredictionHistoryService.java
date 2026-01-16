package com.hackathon.churninsight.service;

import com.hackathon.churninsight.dto.response.PredictionHistoryDTO;
import java.util.List;

public interface PredictionHistoryService {
    List<PredictionHistoryDTO> latest(int page, int size);
    List<PredictionHistoryDTO> byCustomer(String customerId, int page, int size);
    void clearAll(); // opcional
}
