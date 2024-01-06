package com.example.accountservice.repository;

import com.example.accountservice.model.Beneficiary;
import com.example.accountservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary,Long> {
}
