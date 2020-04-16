package com.demo.ad.mediation.models.entity;

import com.demo.ad.mediation.models.dto.AdNetworkDTO;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Accessors(fluent = true)
@Builder
@ToString
public class AdNetwork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String externalId;

    @NotNull
    private String name;

    @NotNull
    private Long score;

    @Builder.Default
    @CreatedDate
    private final LocalDateTime dateCreated = LocalDateTime.now();

    @Builder.Default
    @LastModifiedDate
    private final LocalDateTime dateUpdated = LocalDateTime.now();

    public static AdNetwork withUpdatedNameOrPriority(AdNetwork adNetwork, AdNetworkDTO adNetworkDTO) {
        return AdNetwork.builder()
                .id(adNetwork.id)
                .externalId(adNetwork.externalId)
                .name(adNetworkDTO.getName())
                .score(adNetworkDTO.getScore())
                .dateCreated(adNetwork.dateCreated)
                .dateUpdated(LocalDateTime.now())
                .build();
    }

    public boolean compareToDTO(AdNetworkDTO dto) {
        return this.name.equals(dto.getName()) && this.externalId.equals(dto.getExternalId()) && this.score.equals(dto.getScore());
    }
}
