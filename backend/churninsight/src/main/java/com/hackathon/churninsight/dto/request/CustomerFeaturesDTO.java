package com.hackathon.churninsight.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hackathon.churninsight.domain.enums.PaymentMethod;
import com.hackathon.churninsight.domain.enums.SubscriptionType;
import jakarta.validation.constraints.*;

public record CustomerFeaturesDTO(
        @NotNull(message = "El tipo de suscripción es obligatorio")
        @JsonProperty("subscription_type")
        SubscriptionType subscriptionType,

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

        @NotNull(message = "El método de pago es obligatorio")
        @JsonProperty("payment_method")
        PaymentMethod paymentMethod
) {
    // Validación cruzada Plan vs Precio
    @AssertTrue(message = "La tarifa mensual no coincide con el plan seleccionado")
    private boolean isPlanPriceConsistent() {
        if (subscriptionType == null || monthlyFee == null) return true;
        return Math.abs(monthlyFee - subscriptionType.getPrice()) < 0.01;
    }
}
