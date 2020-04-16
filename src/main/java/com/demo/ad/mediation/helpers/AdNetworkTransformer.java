package com.demo.ad.mediation.helpers;

import com.demo.ad.mediation.models.dto.AdNetworkDTO;
import com.demo.ad.mediation.models.entity.AdNetwork;
import org.springframework.stereotype.Component;

@Component
public class AdNetworkTransformer implements Transformer<AdNetwork, AdNetworkDTO> {
    @Override
    public AdNetworkDTO toBean(AdNetwork entity) {
        return AdNetworkDTO.builder()
                .externalId(entity.externalId())
                .name(entity.name())
                .score(entity.score())
                .dateCreated(entity.dateCreated())
                .dateUpdated(entity.dateUpdated())
                .build().validate();
    }

    @Override
    public AdNetwork toEntity(AdNetworkDTO bean) {
        bean.validate();
        return AdNetwork.builder()
                .externalId(bean.getExternalId())
                .name(bean.getName())
                .score(bean.getScore())
                .build();
    }
}
