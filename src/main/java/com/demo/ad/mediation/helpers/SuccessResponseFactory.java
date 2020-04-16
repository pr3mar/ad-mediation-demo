package com.demo.ad.mediation.helpers;

import com.demo.ad.mediation.models.dto.SuccessResponse;

public class SuccessResponseFactory {
    private static final String OPERATION_BULK_CREATE = "bulk-create";
    private static final String OPERATION_BULK_UPDATE = "bulk-update";

    public static SuccessResponse buildSuccessfulInsert(long rowsAffected) {
        return SuccessResponse.builder()
                .operation(OPERATION_BULK_CREATE)
                .rowsAffected(rowsAffected)
                .build();
    }

    public static SuccessResponse buildSuccessfulUpdate(long rowsAffected) {
        return SuccessResponse.builder()
                .operation(OPERATION_BULK_UPDATE)
                .rowsAffected(rowsAffected)
                .build();
    }
}
