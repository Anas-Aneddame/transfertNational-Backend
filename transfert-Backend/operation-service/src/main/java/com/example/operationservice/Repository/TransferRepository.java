package com.example.operationservice.Repository;

import com.example.operationservice.Model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransferRepository extends JpaRepository<Transfer,String> {
    @Query("SELECT t FROM Transfer t WHERE t.transferReference=:reference")
    public Transfer findTransferByTransferReference(String reference);
}
