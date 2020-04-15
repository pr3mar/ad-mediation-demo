package com.demo.ad.mediation.controllers;

import com.demo.ad.mediation.models.dto.AdNetworkDTO;
import com.demo.ad.mediation.services.AdNetworkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class AdNetworkController {

    private final AdNetworkService adNetworkService;

    public AdNetworkController(AdNetworkService adNetworkService) {
        this.adNetworkService = adNetworkService;
    }

    @GetMapping("/")
    public ResponseEntity<List<AdNetworkDTO>> read() {
        return ResponseEntity.ok(adNetworkService.findAll());
    }

    @GetMapping("/{networkId}")
    public ResponseEntity<AdNetworkDTO> findByNetworkId(@PathVariable("networkId") String networkId) {
        Optional<AdNetworkDTO> entity = adNetworkService.findByNetworkId(networkId);
        return entity.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    public ResponseEntity<AdNetworkDTO> create(@RequestBody AdNetworkDTO adNetworkDTO) {
        AdNetworkDTO createdNetwork = adNetworkService.create(adNetworkDTO);
        return ResponseEntity.ok(createdNetwork);
    }

    @PutMapping("/")
    public ResponseEntity<AdNetworkDTO> update(@RequestBody AdNetworkDTO adNetworkEntity) {
        Optional<AdNetworkDTO> updatedNetwork = adNetworkService.update(adNetworkEntity);
        if (updatedNetwork.isPresent()) {
            return ResponseEntity.ok(updatedNetwork.get());
        } else {
            return ResponseEntity.status(400).build();
        }
    }
}
