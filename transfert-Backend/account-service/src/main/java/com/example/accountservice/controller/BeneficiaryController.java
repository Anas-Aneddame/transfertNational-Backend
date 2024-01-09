package com.example.accountservice.controller;

import com.example.accountservice.exception.BeneficiaryNotFoundException;
import com.example.accountservice.exception.CustomerNotFoundException;
import com.example.accountservice.model.Beneficiary;
import com.example.accountservice.repository.BeneficiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200/")
@RequestMapping("/beneficiaries")
public class BeneficiaryController {

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @GetMapping
    public List<Beneficiary> getAllBeneficiaries() {
        return beneficiaryRepository.findAll();
    }

    @GetMapping("/{id}")
    public Beneficiary getBeneficiaryById(@PathVariable Long id) {
        return beneficiaryRepository.findById(id)
                .orElseThrow(() -> new BeneficiaryNotFoundException(id));
    }
    @GetMapping("/beneficiaries-customer/{customerId}")
    public List<Beneficiary> getBeneficiariesByCustomerId(@PathVariable Long customerId) {
        return beneficiaryRepository.findByCustomer_Id(customerId);
    }
    @PostMapping
    public Beneficiary createBeneficiary(@RequestBody Beneficiary newBeneficiary) {
        return beneficiaryRepository.save(newBeneficiary);

    }

    @PutMapping("/{id}")
    public Beneficiary updateBeneficiary(@RequestBody Beneficiary updatedBeneficiary, @PathVariable Long id) {
        return beneficiaryRepository.findById(id)
                .map(existingBeneficiary -> {
                    if (updatedBeneficiary.getFirstName() != null) {
                        existingBeneficiary.setFirstName(updatedBeneficiary.getFirstName());
                    }
                    if (updatedBeneficiary.getLastName() != null) {
                        existingBeneficiary.setLastName(updatedBeneficiary.getLastName());
                    }
                    if (updatedBeneficiary.getEmail() != null) {
                        existingBeneficiary.setEmail(updatedBeneficiary.getEmail());
                    }
                    if (updatedBeneficiary.getPhone()!= null) {
                        existingBeneficiary.setPhone(updatedBeneficiary.getPhone());
                    }


                    return beneficiaryRepository.save(existingBeneficiary);
                })
                .orElseThrow(() -> new BeneficiaryNotFoundException(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBeneficiary(@PathVariable Long id) {
        if (beneficiaryRepository.existsById(id)) {
            beneficiaryRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}