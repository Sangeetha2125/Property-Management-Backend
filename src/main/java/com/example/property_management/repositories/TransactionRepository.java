package com.example.property_management.repositories;

import com.example.property_management.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.Date;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, BigInteger> {
    @Query(value = "select t.transaction_time from transaction t join request r on r.id=t.request_id join agreement a on a.request_id=r.id join user u on r.user_id=u.id where a.end_date is null and u.id=:userId order by t.transaction_time desc limit 1", nativeQuery = true)
    Optional<Date> getLastPaidDate(BigInteger userId);
}
