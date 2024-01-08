package com.example.accountservice.controller;

import com.example.accountservice.exception.CustomerNotFoundException;
import com.example.accountservice.model.Customer;
import com.example.accountservice.model.Operation;
import com.example.accountservice.model.Transfer;
import com.example.accountservice.repository.CustomerRepository;
import com.example.accountservice.repository.OperationRepository;
import com.example.accountservice.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private TransferRepository transferRepository;

    @GetMapping("/{customerId}/transfers")
    public ResponseEntity<List<Transfer>> getTransfersByCustomer(@PathVariable Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        List<Transfer> transfers = transferRepository.findAllBySender(customer);
        return new ResponseEntity<>(transfers, HttpStatus.OK);
    }

    @PostMapping
    Customer newCustomer(@RequestBody Customer newCustomer) {
        return customerRepository.save(newCustomer);
    }

    @GetMapping("/customers")
    List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping("/{CustomerId}")
    Customer getCustomerById(@PathVariable Long CustomerId) {
        return customerRepository.findById(CustomerId)
                .orElseThrow(() -> new CustomerNotFoundException(CustomerId));
    }


    @PutMapping("/{customerId}")
    Customer updateCustomer(@RequestBody Customer updatedCustomer, @PathVariable Long customerId) {
        return customerRepository.findById(customerId)
                .map(existingCustomer -> {
                    if (updatedCustomer.getFirstName() != null) {
                        existingCustomer.setFirstName(updatedCustomer.getFirstName());
                    }
                    if (updatedCustomer.getLastName() != null) {
                        existingCustomer.setLastName(updatedCustomer.getLastName());
                    }
                    if (updatedCustomer.getAccountType() != null) {
                        existingCustomer.setAccountType(updatedCustomer.getAccountType());
                    }
                    if (updatedCustomer.getBalance()!= null) {
                        existingCustomer.setBalance(updatedCustomer.getBalance());
                    }
                    if (updatedCustomer.getEmail() != null) {
                        existingCustomer.setEmail(updatedCustomer.getEmail());
                    }
                    if (updatedCustomer.getPhone()!= null) {
                        existingCustomer.setPhone(updatedCustomer.getPhone());
                    }
                    if (updatedCustomer.getCNE() != null) {
                        existingCustomer.setCNE(updatedCustomer.getCNE());
                    }

                    return customerRepository.save(existingCustomer);
                })
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }


    @DeleteMapping("/{CustomerId}")
    String deleteCustomer(@PathVariable Long CustomerId){
        if(!customerRepository.existsById(CustomerId)){
            throw new CustomerNotFoundException(CustomerId);
        }
        customerRepository.deleteById(CustomerId);
        return  "Customer with id "+CustomerId+" has been deleted success.";
    }

}
