package com.hackathon.churninsight.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record PredictResponseDTO(
        @NotBlank
        @JsonProperty("customer_id")
        String customerId,

        @NotNull
        @Valid
        @JsonProperty("prediction")
        PredictionResultDTO prediction
) {}
