package com.demo.ad.mediation.models.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ExceptionResponse {
    private int status;
    private String message;
}
