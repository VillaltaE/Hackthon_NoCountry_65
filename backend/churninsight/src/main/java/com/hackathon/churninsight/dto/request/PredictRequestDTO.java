package com.hackathon.churninsight.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record PredictRequestDTO(
        @NotBlank(message = "El customer_id es obligatorio")
        @JsonProperty("customer_id")
        String customerId,

        @NotNull(message = "El objeto features es obligatorio")
        @Valid
        @JsonProperty("features")
        CustomerFeaturesDTO features
) {}
