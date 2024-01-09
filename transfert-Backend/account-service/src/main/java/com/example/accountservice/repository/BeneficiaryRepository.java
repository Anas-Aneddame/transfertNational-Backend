package com.example.accountservice.repository;

import com.example.accountservice.model.Beneficiary;
import com.example.accountservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary,Long> {

    @Query("SELECT b FROM Beneficiary b WHERE b.customer.CustomerId = :customerId")
    List<Beneficiary> findByCustomer_Id(Long customerId);

}
