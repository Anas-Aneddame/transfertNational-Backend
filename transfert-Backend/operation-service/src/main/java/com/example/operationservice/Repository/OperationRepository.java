package com.example.operationservice.Repository;

import com.example.operationservice.Enum.OperationType;
import com.example.operationservice.Enum.TransferType;
import com.example.operationservice.Model.Operation;
import com.example.operationservice.Model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operation,Long> {
    @Query("SELECT o FROM Operation o WHERE o.transferReference.transferReference=:transferReference and o.transferType=:transferType")
    public List<Operation> findAllByTransferReferenceAndTransferType(String transferReference, TransferType transferType);
}
