package com.demo.ad.mediation.repositories;

import com.demo.ad.mediation.models.entity.AdNetwork;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdNetworkRepository extends PagingAndSortingRepository<AdNetwork, Long> {

    @Query("SELECT network FROM AdNetwork network WHERE network.externalId IN :externalId ORDER BY network.externalId ASC")
    Optional<AdNetwork> findByExternalId(@Param("externalId") String id);

    @Query("SELECT network FROM AdNetwork network WHERE network.externalId IN :externalId ORDER BY network.externalId ASC")
    List<AdNetwork> findByExternalIds(@Param("externalId") List<String> id);

    @Query("SELECT COUNT(network) FROM AdNetwork network WHERE network.externalId IN :externalId ")
    Long countByExternalIds(@Param("externalId") List<String> id);

    @Modifying
    @Query("UPDATE AdNetwork network SET network.score = :score WHERE network.externalId = :externalId")
    int updateScoreForExternalId(@Param("externalId") String externalId, @Param("score") long score);
}
