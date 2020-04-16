package com.demo.ad.mediation.services;

import com.demo.ad.mediation.models.dto.AdNetworkDTO;
import com.demo.ad.mediation.models.entity.AdNetwork;
import com.demo.ad.mediation.repositories.AdNetworkRepository;
import com.demo.ad.mediation.components.AdNetworkTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdNetworkService {

    private final AdNetworkRepository repository;
    private final AdNetworkTransformer transformer;

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

    public AdNetworkDTO findByNetworkId(String id) {
        List<AdNetwork> networks = repository.findByNetworkIds(Collections.singletonList(id));
        if (networks.size() == 1) {
            return transformer.toBean(networks.get(0));
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Transactional
    public AdNetworkDTO create(AdNetworkDTO adNetworkDTO) {
        AdNetwork entity = transformer.toEntity(adNetworkDTO);
        return transformer.toBean(repository.save(entity));
    }

    @Transactional
    public List<AdNetworkDTO> bulkCreate(List<AdNetworkDTO> adNetworkDTO) {
        List<AdNetwork> entity = transformer.toEntity(adNetworkDTO);
        return transformer.toBean(repository.saveAll(entity));
    }

    @Transactional
    public AdNetworkDTO updateScore(String networkId, long score) {
        Optional<AdNetwork> adNetworkOptional = repository.findByNetworkId(networkId);
        if (adNetworkOptional.isPresent()) {
            if (repository.updateScoreForNetworkId(networkId, score) == 1) {
                return transformer.toBean(adNetworkOptional.get()).score(score);
            }
        }
        throw new EntityNotFoundException();
    }

    @Transactional
    public AdNetworkDTO update(String networkId, AdNetworkDTO adNetworkDTO) {
        return update(AdNetworkDTO.builder()
                .networkId(networkId)
                .name(adNetworkDTO.name())
                .score(adNetworkDTO.score())
                .build().validate());
    }

    @Transactional
    public List<AdNetworkDTO> updateAll(List<AdNetworkDTO> networkDTOS) {
        List<String> networkIds = networkDTOS.stream().map(AdNetworkDTO::networkId).collect(Collectors.toList());
        Long numEntities = repository.countByNetworkIds(networkIds);
        if (numEntities == networkDTOS.size()) {
            return networkDTOS.stream().map(networkDTO -> {
                networkDTO.validate();
                return this.update(networkDTO);
            }).collect(Collectors.toList());
        } else {
            throw new EntityNotFoundException("Some of the provided entities are missing. Try creating it?");
        }
    }

    private AdNetworkDTO update(AdNetworkDTO adNetworkDTO) {
        List<AdNetwork> entities = repository.findByNetworkIds(Collections.singletonList(adNetworkDTO.networkId()));
        if (entities.isEmpty()) {
            throw new EntityNotFoundException();
        }
        AdNetwork entity = entities.get(0);
        if (!entity.compareToDTO(adNetworkDTO)) {
            AdNetwork saved = repository.save(AdNetwork.withUpdatedNameOrPriority(entity, adNetworkDTO));
            return transformer.toBean(saved);
        }
        return adNetworkDTO;
    }
}
