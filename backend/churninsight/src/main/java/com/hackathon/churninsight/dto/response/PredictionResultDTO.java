package com.hackathon.churninsight.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PredictionResultDTO(
        @JsonProperty("label")
        String label,

        @JsonProperty("probability")
        double probability
) {}
