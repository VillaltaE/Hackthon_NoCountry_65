package com.hackathon.churninsight.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public record CustomerFeaturesDTO(
        @NotBlank(message = "El tipo de suscripción es obligatorio")
        @Pattern(
                regexp = "^(Basic|Standard|Premium)$",
                message = "Solo se permite: Basic, Standard o Premium"
        )
        @JsonProperty("subscription_type")
        String subscriptionType,

        @NotNull
        @DecimalMin(
                value = "0.0",
                inclusive = false,
                message = "Las horas vistas deben ser mayores a 0"
        )
        @JsonProperty("watch_hours")
        Double watchHours,

        @NotNull
        @Min(value = 0, message = "Los días desde el último login no pueden ser negativos")
        @JsonProperty("last_login_days")
        Integer lastLoginDays,

        @NotNull
        @DecimalMin(
                value = "0.0",
                inclusive = false,
                message = "La tarifa mensual debe ser mayor a 0"
        )
        @JsonProperty("monthly_fee")
        Double monthlyFee,

        @NotNull
        @Min(1)
        @Max(5)
        @JsonProperty("number_of_profiles")
        Integer numberOfProfiles,

        @NotNull
        @DecimalMin(
                value = "0.0",
                inclusive = false
        )
        @JsonProperty("avg_watch_time_per_day")
        Double avgWatchTimePerDay,

        @NotBlank
        @Pattern(
                regexp = "^(Credit Card|Debit Card|PayPal|Gift Card|Crypto)$",
                message = "Método de pago inválido"
        )
        @JsonProperty("payment_method")
        String paymentMethod
) {
    // Validación cruzada Plan vs Precio
    @AssertTrue(message = "La tarifa mensual no coincide con el plan seleccionado")
    private boolean isPlanPriceConsistent() {
        if (subscriptionType == null || monthlyFee == null) return false;

        return switch (subscriptionType) {
            case "Basic" -> Math.abs(monthlyFee - 8.99) < 0.01;
            case "Standard" -> Math.abs(monthlyFee - 13.99) < 0.01;
            case "Premium" -> Math.abs(monthlyFee - 17.99) < 0.01;
            default -> false;
        };
    }
}
