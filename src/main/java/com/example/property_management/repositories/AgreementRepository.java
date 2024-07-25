package com.example.property_management.repositories;

import com.example.property_management.models.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface AgreementRepository extends JpaRepository<Agreement, BigInteger> {

    @Query(value = "select a.id, a.request_id, a.start_date, a.end_date, a.number_of_years from agreement a join request r ON r.id = a.request_id WHERE r.user_id = :userId AND r.type <> 'BUY' AND a.end_date IS NULL ORDER BY a.start_date DESC LIMIT 1", nativeQuery = true)
    Optional<Agreement> findCurrentAgreement(BigInteger userId);

    @Query(value = "select a from Agreement a join request r on r.id = a.request.id and r.unit.property.owner.id = :userId and a.request.type<>'BUY' where endDate = null")
    List<Agreement> getCurrentAgreementsOfOwner(BigInteger userId);
}
