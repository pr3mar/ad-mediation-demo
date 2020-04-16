package com.demo.ad.mediation.models.entity;

import com.demo.ad.mediation.models.dto.AdNetworkDTO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
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

    @Column(unique = true)
    private String networkId;

    private @NotNull String name;

    private @NotNull Long score;

    @CreatedDate @Builder.Default
    private final LocalDateTime dateCreated = LocalDateTime.now();

    @LastModifiedDate @Builder.Default
    private final LocalDateTime dateUpdated = LocalDateTime.now();

    public static AdNetwork withUpdatedNameOrPriority(AdNetwork adNetwork, AdNetworkDTO adNetworkDTO) {
        return AdNetwork.builder()
                .entityId(adNetwork.entityId)
                .networkId(adNetwork.networkId)
                .name(adNetworkDTO.getName())
                .score(adNetworkDTO.getScore())
                .dateCreated(adNetwork.dateCreated)
                .dateUpdated(LocalDateTime.now())
                .build();
    }

    public boolean compareToDTO(AdNetworkDTO dto) {
        return this.name.equals(dto.getName()) && this.networkId.equals(dto.getNetworkId()) && this.score.equals(dto.getScore());
    }
}
