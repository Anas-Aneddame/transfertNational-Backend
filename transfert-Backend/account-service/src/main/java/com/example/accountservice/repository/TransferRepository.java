package com.example.accountservice.repository;


import com.example.accountservice.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, String> {

   // List<Transfer> findAllBySenderId(Long senderId);
}