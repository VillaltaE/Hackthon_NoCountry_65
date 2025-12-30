package com.hackathon.churninsight.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PredictResponseDTO(
        @JsonProperty("customer_id")
        String customerId,

        @JsonProperty("prediction")
        PredictionResultDTO prediction
) {}
