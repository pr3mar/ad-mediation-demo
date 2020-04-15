package com.demo.ad.mediation.controllers;

import com.demo.ad.mediation.models.AdNetwork;
import com.demo.ad.mediation.services.AdNetworkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class AdNetworkController {

    private final AdNetworkService adNetworkService;

    public AdNetworkController(AdNetworkService adNetworkService) {
        this.adNetworkService = adNetworkService;
    }

    @GetMapping("/")
    public List<AdNetwork> read() {
        return adNetworkService.findAll();
    }

    @PostMapping("/")
    public ResponseEntity<AdNetwork> create(@RequestBody AdNetwork adNetwork) {
        AdNetwork createdNetwork = adNetworkService.create(adNetwork);
        return ResponseEntity.ok(createdNetwork);
    }

    @PutMapping("/")
    public ResponseEntity<AdNetwork> update(@RequestBody AdNetwork adNetwork) {
        AdNetwork updatedNetwork = adNetworkService.update(adNetwork);
        return ResponseEntity.ok(updatedNetwork);
    }
}
