package com.example.operationservice.Repository;

import com.example.operationservice.Model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer,String> {
    @Query("SELECT t FROM Transfer t WHERE t.transferReference=:reference")
    public Transfer findTransferByTransferReference(String reference);

    @Query("SELECT t FROM Transfer t WHERE t.sender.CustomerId=:senderId")
    public List<Transfer> findTransferBySenderId(Long senderId);

    @Query("SELECT t FROM Transfer t WHERE t.sender.Email=:email or t.receiver.email=:email")
    public List<Transfer> findTransferByEmail(String email);
}
