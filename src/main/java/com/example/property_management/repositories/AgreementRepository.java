package com.example.property_management.repositories;

import com.example.property_management.models.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface AgreementRepository extends JpaRepository<Agreement, BigInteger> {
    @Query(value = "select a from Agreement a join request r on r.id = a.request.id and r.user.id = :userId and a.request.type<>'BUY' where endDate = null order by startDate desc limit 1")
    Optional<Agreement> findCurrentAgreement(BigInteger userId);

    @Query(value = "select a from Agreement a join request r on r.id = a.request.id and r.unit.property.owner.id = :userId and a.request.type<>'BUY' where endDate = null")
    List<Agreement> getCurrentAgreementsOfOwner(BigInteger userId);
}
