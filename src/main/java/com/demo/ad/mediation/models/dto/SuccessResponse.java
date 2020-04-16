package com.demo.ad.mediation.models.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Builder
@RequiredArgsConstructor
public class SuccessResponse {
    private final long rowsAffected;
    private final String operation;
}
