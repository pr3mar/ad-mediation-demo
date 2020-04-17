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
import org.apache.commons.lang3.Validate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdNetworkService {
    private final AdNetworkRepository repository;
    private final AdNetworkTransformer transformer;

    public List<AdNetworkDTO> findAll(int pageNumber, int pageSize) {
        Validate.isTrue(pageNumber >= 0, "Please provide a page number >= 0");
        Validate.isTrue(pageSize >= 0 && pageSize <= 1000, "Please provide a page size on the interval 0 <= pageSize <= 1000");
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

    public List<String> findAllSlim(int pageNumber, int pageSize) {
        return findAll(pageNumber, pageSize).stream().map(AdNetworkDTO::getExternalId).collect(Collectors.toList());
    }

    public AdNetworkDTO findByExternalId(String id) {
        Optional<AdNetwork> networkOption = repository.findByExternalId(id);
        AdNetwork network = networkOption.orElseThrow(() -> new AdNetworkNotFoundException("Ad network with ID " + id + " could not be found."));
        return transformer.toBean(network);
    }

    @Transactional
    public AdNetworkDTO create(AdNetworkDTO adNetworkDTO) {
        AdNetwork entity = transformer.toEntity(adNetworkDTO);
        return transformer.toBean(repository.save(entity));
    }

    @Transactional
    public SuccessResponse bulkCreate(List<AdNetworkDTO> adNetworkDTO) {
        List<AdNetwork> entities = transformer.toEntity(adNetworkDTO);
        long createdEntities = StreamSupport.stream(repository.saveAll(entities).spliterator(), false).count();
        return SuccessResponseFactory.buildSuccessfulInsert(createdEntities);
    }

    @Transactional
    public AdNetworkDTO updateScore(String externalId, long score) {
        Validate.isTrue(score >= 0, "The score must be >= 0.");
        Optional<AdNetwork> adNetworkOptional = repository.findByExternalId(externalId);
        AdNetwork entity = adNetworkOptional.orElseThrow(() -> new AdNetworkNotFoundException("Ad network with ID " + externalId + " could not be found."));
        if (repository.updateScoreForExternalId(externalId, score) == 1) {
            return transformer.toBean(entity).withScore(score);
        }
        log.error("Error updating the score of {} to {}", entity, score);
        throw new RuntimeException("Unexpected amount of entities updated.");
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
            throw new AdNetworkNotFoundException("Some of the provided entities could not be found in the database. Try creating it first?");
        }
        long affectedRows = 0;
        for (AdNetworkDTO networkDTO: networkDTOS) {
            updateInstance(networkDTO.validate());
            affectedRows++;
        }
        return SuccessResponseFactory.buildSuccessfulUpdate(affectedRows);
    }

    private AdNetworkDTO updateInstance(AdNetworkDTO adNetworkDTO) {
        log.info("Updating {}", adNetworkDTO);
        Optional<AdNetwork> entityOption = repository.findByExternalId(adNetworkDTO.getExternalId());
        AdNetwork entity = entityOption.orElseThrow(() -> new AdNetworkNotFoundException("Ad network with ID " + adNetworkDTO.getExternalId() + " could not be found."));
        if (!entity.compareToDTO(adNetworkDTO)) {
            AdNetwork saved = repository.save(AdNetwork.withUpdatedNameOrPriority(entity, adNetworkDTO));
            return transformer.toBean(saved);
        }
        log.error("Entity {} not updated!", entity);
        return adNetworkDTO;
    }
}
