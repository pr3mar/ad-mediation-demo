package com.demo.ad.mediation.repositories;

import com.demo.ad.mediation.models.entity.AdNetwork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdNetworkRepository extends JpaRepository<AdNetwork, Long> {
    @Query("SELECT network FROM AdNetwork network WHERE network.networkId = :networkId")
    Optional<AdNetwork> findByNetworkId(@Param("networkId") String id);
}
