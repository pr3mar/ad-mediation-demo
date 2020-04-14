package com.demo.ad.mediation.controllers;

import com.demo.ad.mediation.models.AdNetwork;
import com.demo.ad.mediation.services.AdNetworkService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class AdNetworkController extends AdNetworkService {

    private final AdNetworkService adNetworkService;

    public AdNetworkController(AdNetworkService adNetworkService) {
        this.adNetworkService = adNetworkService;
    }

    @GetMapping("/")
    public List<AdNetwork> read() {
        return adNetworkService.readAll();
    }
}
