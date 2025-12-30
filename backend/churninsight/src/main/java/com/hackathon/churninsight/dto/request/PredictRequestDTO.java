package com.hackathon.churninsight.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PredictRequestDTO(
        @JsonProperty("customer_id")
        String customerId,
        @JsonProperty("features")
        CustomerFeaturesDTO features
) {}
