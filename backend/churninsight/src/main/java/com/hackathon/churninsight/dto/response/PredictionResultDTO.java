package com.hackathon.churninsight.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public record PredictionResultDTO(
        @NotBlank
        @Pattern(
                regexp = "^(will_churn|will_continue)$",
                message = "Label inválido"
        )
        @JsonProperty("label")
        String label,

        @DecimalMin(value = "0.0", inclusive = true)
        @DecimalMax(value = "1.0", inclusive = true)
        @JsonProperty("probability")
        double probability
) {}
