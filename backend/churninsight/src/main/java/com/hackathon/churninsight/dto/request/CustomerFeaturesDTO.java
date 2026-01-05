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

        @NotNull (message = "Las horas vistas son obligatorias")
        @DecimalMin(value = "0.0", message = "Las horas vistas deben ser mayores a 0")

        @JsonProperty("watch_hours")
        Double watchHours,

        @NotNull(message = "Los días desde el último login son obligatorios")
        @Min(value = 0, message = "Los días desde el último login no pueden ser negativos")
        @JsonProperty("last_login_days")
        Integer lastLoginDays,

        @NotNull(message = "La tarifa mensual es obligatoria")
        @JsonProperty("monthly_fee")
        Double monthlyFee,

        @NotNull(message = "El número de perfiles es obligatorio")
        @Min(value = 1, message = "el valor debe estar entre 1 y 5")
        @Max(value = 5, message = "el valor debe estar entre 1 y 5")
        @JsonProperty("number_of_profiles")
        Integer numberOfProfiles,

        @NotNull(message = "El tiempo promedio por día es obligatorio")
        @DecimalMin(value = "0.0", message = "El tiempo promedio debe ser mayor a 0")
        @DecimalMax(value = "24.0", message = "El tiempo promedio por día no puede exceder 24 horas")
        @JsonProperty("avg_watch_time_per_day")
        Double avgWatchTimePerDay,

        @NotBlank(message = "El método de pago es obligatorio")
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
        if (subscriptionType == null || monthlyFee == null) return true;

        return switch (subscriptionType) {
            case "Basic" -> Math.abs(monthlyFee - 8.99) < 0.01;
            case "Standard" -> Math.abs(monthlyFee - 13.99) < 0.01;
            case "Premium" -> Math.abs(monthlyFee - 17.99) < 0.01;
            default -> false;
        };
    }
}
