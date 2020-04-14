package com.demo.ad.mediation.services;

import com.demo.ad.mediation.models.AdNetwork;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdNetworkService {
    public List<AdNetwork> readAll() {
        return new ArrayList<>();
    }
}
