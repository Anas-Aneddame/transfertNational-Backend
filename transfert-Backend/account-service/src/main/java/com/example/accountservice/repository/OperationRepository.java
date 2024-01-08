package com.example.accountservice.repository;

import com.example.accountservice.model.Operation;
import com.example.accountservice.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Long> {

    List<Operation> findAllByTransferReference(Transfer transferReference);

}
