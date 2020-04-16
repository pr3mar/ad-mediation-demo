package com.demo.ad.mediation.services;

import com.demo.ad.mediation.models.dto.AdNetworkDTO;
import com.demo.ad.mediation.models.dto.SuccessResponse;
import com.demo.ad.mediation.models.entity.AdNetwork;
import com.demo.ad.mediation.repositories.AdNetworkRepository;
import com.demo.ad.mediation.components.AdNetworkTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdNetworkService {
    private static final String OPERATION_BULK_UPDATE = "bulk-update";
    private static final String OPERATION_BULK_INSERT = "bulk-insert";

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
    public SuccessResponse bulkCreate(List<AdNetworkDTO> adNetworkDTO) {
        List<AdNetwork> entities = transformer.toEntity(adNetworkDTO);
        List<AdNetworkDTO> beans = transformer.toBean(repository.saveAll(entities));
        return new SuccessResponse(beans.size(), OPERATION_BULK_INSERT);
    }

    @Transactional
    public AdNetworkDTO updateScore(String networkId, long score) {
        Optional<AdNetwork> adNetworkOptional = repository.findByNetworkId(networkId);
        if (adNetworkOptional.isPresent()) {
            if (repository.updateScoreForNetworkId(networkId, score) == 1) {
                AdNetworkDTO bean = transformer.toBean(adNetworkOptional.get());
                bean.setScore(score);
                return bean;
            }
        }
        throw new EntityNotFoundException();
    }

    @Transactional
    public AdNetworkDTO updateInstance(String networkId, AdNetworkDTO adNetworkDTO) {
        return updateInstance(AdNetworkDTO.builder()
                .networkId(networkId)
                .name(adNetworkDTO.getName())
                .score(adNetworkDTO.getScore())
                .build().validate());
    }

    @Transactional
    public SuccessResponse updateAll(List<AdNetworkDTO> networkDTOS) {
        List<String> networkIds = networkDTOS.stream().map(AdNetworkDTO::getNetworkId).collect(Collectors.toList());
        Long numEntities = repository.countByNetworkIds(networkIds);
        if (numEntities != networkDTOS.size()) {
            throw new EntityNotFoundException("Some of the provided entities are missing. Try creating it?");
        }
        long affectedRows = networkDTOS.stream().map(networkDTO -> {
            networkDTO.validate();
            return this.updateInstance(networkDTO);
        }).count();
        return new SuccessResponse(affectedRows, OPERATION_BULK_UPDATE);
    }

    private AdNetworkDTO updateInstance(AdNetworkDTO adNetworkDTO) {
        List<AdNetwork> entities = repository.findByNetworkIds(Collections.singletonList(adNetworkDTO.getName()));
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
