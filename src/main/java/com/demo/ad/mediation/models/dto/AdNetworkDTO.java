package com.demo.ad.mediation.models.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.Validate;

import java.time.LocalDateTime;

@Data
@Builder
@Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AdNetworkDTO {

    private String networkId;

    private String name;

    private Long score;

    @Builder.Default
    private LocalDateTime dateCreated = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime dateUpdated = LocalDateTime.now();

    public AdNetworkDTO validate() {
        Validate.notNull(networkId);
        Validate.notNull(name);
        Validate.notNull(score);
        return this;
    }
}
