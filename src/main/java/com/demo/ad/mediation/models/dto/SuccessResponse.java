package com.demo.ad.mediation.models.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SuccessResponse {
    private final long rowsAffected;
    private final String operation;
}
