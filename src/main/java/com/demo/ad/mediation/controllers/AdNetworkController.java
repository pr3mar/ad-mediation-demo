package com.demo.ad.mediation.controllers;

import com.demo.ad.mediation.models.dto.AdNetworkDTO;
import com.demo.ad.mediation.models.dto.SuccessResponse;
import com.demo.ad.mediation.services.AdNetworkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/networks")
@Slf4j
@RequiredArgsConstructor
public class AdNetworkController extends AbstractController {

    private final AdNetworkService adNetworkService;
    private final String DEFAULT_PAGE = "0";
    private final String DEFAULT_PAGE_SIZE = "100";

    @GetMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> helloWorld() {
        log.info("Dummy page requested.");
        return ResponseEntity.ok(new SuccessResponse(0, "hello-world"));
    }

    @GetMapping(value = "/all", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AdNetworkDTO>> read(
        @RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
        @RequestParam(value = "pageSize", required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize
    ) {
        List<AdNetworkDTO> dtos = adNetworkService.findAll(page, pageSize);
        log.info("Loaded {} networks successfully.", dtos.size());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping(value = "/list", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> readSlim(
        @RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
        @RequestParam(value = "pageSize", required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize
    ) {
        List<String> ids = adNetworkService.findAllSlim(page, pageSize);
        log.info("Loaded {} network IDs successfully.", ids.size());
        return ResponseEntity.ok(ids);
    }

    @GetMapping(value = "/{externalId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AdNetworkDTO> findByExternalId(@PathVariable("externalId") String externalId) {
        AdNetworkDTO dto = adNetworkService.findByExternalId(externalId);
        log.info("Successfully loaded {}", dto);
        return ResponseEntity.ok(dto);
    }

    @PostMapping(value = "/create", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AdNetworkDTO> create(@RequestBody AdNetworkDTO adNetworkDTO) {
        AdNetworkDTO createdNetwork = adNetworkService.create(adNetworkDTO);
        log.info("Created a new network successfully {}", createdNetwork);
        return ResponseEntity.status(201).body(createdNetwork);
    }

    @PostMapping(value = "/create/bulk", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> create(@RequestBody List<AdNetworkDTO> adNetworkDTOs) {
        SuccessResponse successResponse = adNetworkService.bulkCreate(adNetworkDTOs);
        log.info("Operation: {} done successfully for {} entities", successResponse.getOperation(), successResponse.getRowsAffected());
        return ResponseEntity.ok(successResponse);
    }

    @PutMapping(value = "/update/{externalId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AdNetworkDTO> updateById(@PathVariable("externalId") String externalId, @RequestBody AdNetworkDTO adNetworkEntity) {
        AdNetworkDTO updatedNetwork = adNetworkService.updateInstance(externalId, adNetworkEntity);
        log.info("Updated a network successfully {} ", updatedNetwork);
        return ResponseEntity.ok(updatedNetwork);
    }

    @PutMapping(value = "/update/{externalId}/score/{score}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AdNetworkDTO> updatePriorityById(@PathVariable("externalId") String externalId, @PathVariable("score") long score) {
        AdNetworkDTO updatedNetwork = adNetworkService.updateScore(externalId, score);
        log.info("Successfully updated the score of {} to {} ", externalId, score);
        return ResponseEntity.ok(updatedNetwork);
    }

    @PutMapping(value = "/update", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> updateAll(@RequestBody List<AdNetworkDTO> adNetworkEntity) {
        SuccessResponse successResponse = adNetworkService.updateAll(adNetworkEntity);
        log.info("Operation: {} done successfully for {} entities", successResponse.getOperation(), successResponse.getRowsAffected());
        return ResponseEntity.ok(successResponse);
    }
}
