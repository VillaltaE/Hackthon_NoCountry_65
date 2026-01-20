package com.hackathon.churninsight.service.impl;

import com.hackathon.churninsight.domain.PredictionHistory;
import com.hackathon.churninsight.dto.response.KPIsDTO;
import com.hackathon.churninsight.repository.PredictionHistoryRepository;
import com.hackathon.churninsight.service.KPIsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KPIsServiceImpl implements KPIsService {

    private final PredictionHistoryRepository historyRepo;

    @Override
    public KPIsDTO getKPIs() {
        List<PredictionHistory> all = historyRepo.findAll();

        long total = all.size();
        long high = all.stream().filter(h -> "Riesgo alto".equalsIgnoreCase(h.getPredictionLabel())).count();
        long medium = all.stream().filter(h -> "Riesgo medio".equalsIgnoreCase(h.getPredictionLabel())).count();
        long low = all.stream().filter(h -> "Riesgo bajo".equalsIgnoreCase(h.getPredictionLabel())).count();

        double churnRate = total > 0 ? (double) high / total * 100 : 0;

        return new KPIsDTO(total, high, medium, low, churnRate);
    }
}
