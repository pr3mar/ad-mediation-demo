package com.demo.ad.mediation.models.dto;

import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.Validate;

import java.time.LocalDateTime;

@Value
@Builder
public class AdNetworkDTO {

    private String externalId;

    private String name;

    private Long score;

    @Builder.Default
    private LocalDateTime dateCreated = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime dateUpdated = LocalDateTime.now();

    public AdNetworkDTO validate() {
        Validate.notNull(externalId, "Please provide an ID for the AdNetwork.");
        Validate.notNull(name, "Please provide an AdNetwork name.");
        Validate.notNull(score, "Please provide the score.");
        Validate.isTrue(!name.equals(""), "The name cannot be empty.");
        Validate.isTrue(score >= 0, "The score must be >= 0.");
        return this;
    }

    public AdNetworkDTO withScore(Long score) {
        return AdNetworkDTO.builder()
                .externalId(this.getExternalId())
                .name(this.getName())
                .score(this.getScore())
                .dateCreated(this.getDateCreated())
                .dateUpdated(this.getDateUpdated())
                .build();
    }
}
