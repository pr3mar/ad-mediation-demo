package com.demo.ad.mediation.models;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter @Setter
@RequiredArgsConstructor
@Accessors(fluent = true)
@ToString
@EqualsAndHashCode
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AdNetwork {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private @NonNull long id;

}
