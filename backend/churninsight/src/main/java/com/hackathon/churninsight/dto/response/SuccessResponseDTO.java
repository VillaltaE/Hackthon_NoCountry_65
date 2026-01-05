package com.hackathon.churninsight.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record SuccessResponseDTO<T>(
        LocalDateTime timestamp,
        int status,
        String message,
        T data,
        String path
) {}

