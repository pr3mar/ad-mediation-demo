package com.demo.ad.mediation.repositories;

import com.demo.ad.mediation.models.entity.AdNetwork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdNetworkRepository extends JpaRepository<AdNetwork, Long> {

    @Query("SELECT network FROM AdNetwork network WHERE network.networkId IN :networkId ORDER BY network.networkId ASC")
    Optional<AdNetwork> findByNetworkId(@Param("networkId") String id);

    @Query("SELECT network FROM AdNetwork network WHERE network.networkId IN :networkId ORDER BY network.networkId ASC")
    List<AdNetwork> findByNetworkIds(@Param("networkId") List<String> id);

    @Query("SELECT COUNT(network) FROM AdNetwork network WHERE network.networkId IN :networkId ")
    Long countByNetworkIds(@Param("networkId") List<String> id);

    @Modifying
    @Query("UPDATE AdNetwork network SET network.score = :score WHERE network.networkId = :networkId")
    int updateScoreForNetworkId(@Param("networkId") String networkId, @Param("score") long score);
}
