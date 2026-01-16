package com.hackathon.churninsight.service;

import com.hackathon.churninsight.dto.response.PredictionHistoryDTO;
import java.util.List;

public interface PredictionHistoryService {
    List<PredictionHistoryDTO> latest();
    List<PredictionHistoryDTO> byCustomer(String customerId);
    void clearAll(); // Opcional para borrar los datos
}
