package com.demo.ad.mediation.services;

import com.demo.ad.mediation.models.AdNetwork;
import com.demo.ad.mediation.repositories.AdNetworkRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdNetworkService {

    private final AdNetworkRepository repository;

    public AdNetworkService(AdNetworkRepository repository) {
        this.repository = repository;
    }

    public List<AdNetwork> findAll() {
        return repository.findAll();
    }

    public AdNetwork create(AdNetwork adNetwork) {
        return repository.save(adNetwork);
    }

    public AdNetwork update(AdNetwork adNetwork) {
        Optional<AdNetwork> entity = repository.findById(adNetwork.entityId());
        return entity.map(network -> repository.save(AdNetwork.withUpdatedPriority(network, adNetwork.score()))).orElse(null);
    }

}
