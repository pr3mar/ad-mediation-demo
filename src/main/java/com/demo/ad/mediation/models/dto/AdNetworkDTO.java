package com.demo.ad.mediation.models.dto;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.Validate;

import java.time.LocalDateTime;

@Data
@Builder
public class AdNetworkDTO {

    private String networkId;

    private String name;

    private Long score;

    @Builder.Default
    private LocalDateTime dateCreated = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime dateUpdated = LocalDateTime.now();

    public AdNetworkDTO validate() {
        Validate.notNull(networkId, "Please provide an ID for the AdNetwork.");
        Validate.notNull(name, "Please provide an AdNetwork name.");
        Validate.notNull(score, "Please provide the score.");
        Validate.isTrue(!name.equals(""), "The name cannot be empty.");
        Validate.isTrue(score >= 0, "The score must be >= 0.");
        return this;
    }
}
