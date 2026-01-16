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
    public List<PredictionHistoryDTO> latest(@RequestParam(defaultValue = "0") int page, 
                                              @RequestParam(defaultValue = "20") int size) {
        return service.latest(page, size);
    }

    @GetMapping("/{customerId}")
    public List<PredictionHistoryDTO> byCustomer(@PathVariable String customerId, 
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "20") int size) {
        return service.byCustomer(customerId, page, size);
    }

    @DeleteMapping
    public void clear() {
        service.clearAll();
    }
}
