package com.example.operationservice.Repository;

import com.example.operationservice.Model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation,Long> {
}
