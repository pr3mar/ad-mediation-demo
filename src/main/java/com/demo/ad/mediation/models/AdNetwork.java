package com.demo.ad.mediation.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@ToString
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AdNetwork {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long entityId;

    private @NotNull String networkId;

    private @NotNull String name;

    private @NotNull Long score;

    @CreatedDate @Builder.Default
    private LocalDateTime dateCreated = LocalDateTime.now();

    @LastModifiedDate @Builder.Default
    private LocalDateTime dateUpdated = LocalDateTime.now();

    public static AdNetwork withUpdatedPriority(AdNetwork adNetwork, Long newScore) {
        return AdNetwork.builder()
                .entityId(adNetwork.entityId)
                .networkId(adNetwork.networkId)
                .name(adNetwork.name)
                .score(newScore)
                .dateCreated(adNetwork.dateCreated)
                .dateUpdated(LocalDateTime.now())
                .build();
    }
}
