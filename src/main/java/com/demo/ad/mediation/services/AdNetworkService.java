package com.demo.ad.mediation.services;

import com.demo.ad.mediation.helpers.AdNetworkNotFoundException;
import com.demo.ad.mediation.helpers.SuccessResponseFactory;
import com.demo.ad.mediation.models.dto.AdNetworkDTO;
import com.demo.ad.mediation.models.dto.SuccessResponse;
import com.demo.ad.mediation.models.entity.AdNetwork;
import com.demo.ad.mediation.repositories.AdNetworkRepository;
import com.demo.ad.mediation.helpers.AdNetworkTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdNetworkService {
    private static final String OPERATION_BULK_UPDATE = "bulk-update";
    private static final String OPERATION_BULK_INSERT = "bulk-insert";

    private final AdNetworkRepository repository;
    private final AdNetworkTransformer transformer;

    public List<AdNetworkDTO> findAll(int pageNumber, int pageSize) {
        return transformer.toBean(
            repository.findAll(
                PageRequest.of(
                    pageNumber,
                    pageSize,
                    Sort.by("score").descending().and(
                        Sort.by("dateUpdated").descending().and(
                                Sort.by("externalId").ascending()
                        )
                    )
                )
            ).toList()
        );
    }

    public AdNetworkDTO findByNetworkId(String id) {
        Optional<AdNetwork> network = repository.findByExternalId(id);
        if(network.isPresent()) {
            return transformer.toBean(network.get());
        } else {
            throw new AdNetworkNotFoundException();
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
        long createdEntities = Stream.of(repository.saveAll(entities)).count();
        return SuccessResponseFactory.buildSuccessfulInsert(createdEntities);
    }

    @Transactional
    public AdNetworkDTO updateScore(String externalId, long score) {
        Optional<AdNetwork> adNetworkOptional = repository.findByExternalId(externalId);
        AdNetwork entity = adNetworkOptional.orElseThrow(AdNetworkNotFoundException::new);
        if (repository.updateScoreForExternalId(externalId, score) == 1) {
            return transformer.toBean(entity).withScore(score);
        }
        log.error("Error updating the score of {} to {}", entity, score);
        throw new RuntimeException("Unexpected amount of entities updated :O");
    }

    @Transactional
    public AdNetworkDTO updateInstance(String externalId, AdNetworkDTO adNetworkDTO) {
        return updateInstance(AdNetworkDTO.builder()
                .externalId(externalId)
                .name(adNetworkDTO.getName())
                .score(adNetworkDTO.getScore())
                .build().validate());
    }

    @Transactional
    public SuccessResponse updateAll(List<AdNetworkDTO> networkDTOS) {
        List<String> externalIds = networkDTOS.stream().map(AdNetworkDTO::getExternalId).collect(Collectors.toList());
        Long numEntities = repository.countByExternalIds(externalIds);
        if (numEntities != networkDTOS.size()) {
            throw new AdNetworkNotFoundException("Some of the provided entities are missing. Try creating it?");
        }
        long affectedRows = networkDTOS.stream().map(networkDTO -> {
            networkDTO.validate();
            return this.updateInstance(networkDTO);
        }).count();
        return SuccessResponseFactory.buildSuccessfulUpdate(affectedRows);
    }

    private AdNetworkDTO updateInstance(AdNetworkDTO adNetworkDTO) {
        Optional<AdNetwork> entityOption = repository.findByExternalId(adNetworkDTO.getExternalId());
        AdNetwork entity = entityOption.orElseThrow(AdNetworkNotFoundException::new);
        if (!entity.compareToDTO(adNetworkDTO)) {
            AdNetwork saved = repository.save(AdNetwork.withUpdatedNameOrPriority(entity, adNetworkDTO));
            return transformer.toBean(saved);
        }
        return adNetworkDTO;
    }
}
