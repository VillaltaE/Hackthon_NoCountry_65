package com.hackathon.churninsight.service;

import com.hackathon.churninsight.dto.request.PredictRequestDTO;
import com.hackathon.churninsight.dto.response.PredictResponseDTO;

public interface PredictService {
    PredictResponseDTO predict(PredictRequestDTO request);
}
