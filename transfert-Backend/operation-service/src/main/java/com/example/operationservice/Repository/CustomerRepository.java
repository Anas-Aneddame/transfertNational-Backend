package com.example.operationservice.Repository;

import com.example.operationservice.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    @Query("SELECT c from Customer c WHERE c.Email=:email")
    public Customer findByEmail(String email);
}
