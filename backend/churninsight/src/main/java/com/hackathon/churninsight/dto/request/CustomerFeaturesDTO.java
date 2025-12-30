package com.hackathon.churninsight.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CustomerFeaturesDTO(
        @JsonProperty("subscription_type")
        String subscriptionType,

        @JsonProperty("watch_hours")
        double watchHours,

        @JsonProperty("last_login_days")
        int lastLoginDays,

        @JsonProperty("monthly_fee")
        double monthlyFee,

        @JsonProperty("number_of_profiles")
        int numberOfProfiles,

        @JsonProperty("avg_watch_time_per_day")
        double avgWatchTimePerDay,

        @JsonProperty("payment_method")
        String paymentMethod
) {}
