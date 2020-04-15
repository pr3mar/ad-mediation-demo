package com.demo.ad.mediation.services;

import com.demo.ad.mediation.models.dto.AdNetworkDTO;
import com.demo.ad.mediation.models.entity.AdNetwork;
import com.demo.ad.mediation.repositories.AdNetworkRepository;
import com.demo.ad.mediation.components.AdNetworkTransformer;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdNetworkService {

    private final AdNetworkRepository repository;
    private final AdNetworkTransformer transformer;

    public AdNetworkService(
        AdNetworkRepository repository,
        AdNetworkTransformer transformer
    ) {
        this.repository = repository;
        this.transformer = transformer;
    }

    public List<AdNetworkDTO> findAll() {
        return transformer.toBean(
            repository.findAll(
                Sort.by(
                    Sort.Order.desc("score"),
                    Sort.Order.desc("dateUpdated"),
                    Sort.Order.asc("networkId")
                )
            )
        );
    }

    public AdNetworkDTO create(AdNetworkDTO adNetworkDTO) {
        AdNetwork entity = transformer.toEntity(adNetworkDTO);
        return transformer.toBean(repository.save(entity));
    }

    public Optional<AdNetworkDTO> findByNetworkId(String id) {
        return repository.findByNetworkId(id).map(transformer::toBean);
    }

    public Optional<AdNetworkDTO> update(AdNetworkDTO adNetworkDTO) {
        Optional<AdNetwork> entity = repository.findByNetworkId(adNetworkDTO.networkId());
        return entity
            .map(network -> {
                AdNetwork saved = repository.save(AdNetwork.withUpdatedPriority(network, adNetworkDTO.score()));
                return transformer.toBean(saved);
            });
    }
}
