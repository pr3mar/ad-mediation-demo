package com.demo.ad.mediation.controllers;

import com.demo.ad.mediation.models.dto.AdNetworkDTO;
import com.demo.ad.mediation.models.dto.SuccessResponse;
import com.demo.ad.mediation.services.AdNetworkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/networks")
@Slf4j
@RequiredArgsConstructor
public class AdNetworkController extends AbstractController {

    private final AdNetworkService adNetworkService;

    @GetMapping(value = "/all", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AdNetworkDTO>> read() {
        return ResponseEntity.ok(adNetworkService.findAll());
    }

    @GetMapping(value = "/{networkId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AdNetworkDTO> findByNetworkId(@PathVariable("networkId") String networkId) {
        AdNetworkDTO dto = adNetworkService.findByNetworkId(networkId);
        return ResponseEntity.ok(dto);
    }

    @PostMapping(value = "/create", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AdNetworkDTO> create(@RequestBody AdNetworkDTO adNetworkDTO) {
        AdNetworkDTO createdNetwork = adNetworkService.create(adNetworkDTO);
        return ResponseEntity.ok(createdNetwork);
    }

    @PostMapping(value = "/create/bulk", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> create(@RequestBody List<AdNetworkDTO> adNetworkDTOs) {
        SuccessResponse successResponse = adNetworkService.bulkCreate(adNetworkDTOs);
        return ResponseEntity.ok(successResponse);
    }

    @PutMapping(value = "/update/{networkId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AdNetworkDTO> updateById(@PathVariable("networkId") String networkId, @RequestBody AdNetworkDTO adNetworkEntity) {
        AdNetworkDTO updatedNetwork = adNetworkService.updateInstance(networkId, adNetworkEntity);
        return ResponseEntity.ok(updatedNetwork);
    }

    @PutMapping(value = "/update/{networkId}/score/{score}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AdNetworkDTO> updatePriorityById(@PathVariable("networkId") String networkId, @PathVariable("score") long score) {
        AdNetworkDTO updatedNetwork = adNetworkService.updateScore(networkId, score);
        return ResponseEntity.ok(updatedNetwork);
    }

    @PutMapping(value = "/update", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> updateAll(@RequestBody List<AdNetworkDTO> adNetworkEntity) {
        SuccessResponse successResponse = adNetworkService.updateAll(adNetworkEntity);
        return ResponseEntity.ok(successResponse);
    }
}
