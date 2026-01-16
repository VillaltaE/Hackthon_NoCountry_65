package com.hackathon.churninsight.controller;

import com.hackathon.churninsight.dto.response.PredictionHistoryDTO;
import com.hackathon.churninsight.service.PredictionHistoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
public class PredictionHistoryController {

    private final PredictionHistoryService service;

    public PredictionHistoryController(PredictionHistoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<PredictionHistoryDTO> latest() {
        return service.latest();
    }

    @GetMapping("/{customerId}")
    public List<PredictionHistoryDTO> byCustomer(@PathVariable String customerId) {
        return service.byCustomer(customerId);
    }

    @DeleteMapping
    public void clear() {
        service.clearAll();
    }
}
