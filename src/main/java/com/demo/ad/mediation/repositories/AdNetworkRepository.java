package com.demo.ad.mediation.repositories;

import com.demo.ad.mediation.models.AdNetwork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdNetworkRepository extends JpaRepository<AdNetwork, Long> {
}
