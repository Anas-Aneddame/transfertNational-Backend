package com.example.operationservice.Repository;

import com.example.operationservice.Model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer,String> {
}
