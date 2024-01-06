package com.example.operationservice.Repository;

import com.example.operationservice.Model.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary,Long> {
}
