package com.example.accountservice.controller;

import com.example.accountservice.exception.CustomerNotFoundException;
import com.example.accountservice.model.Customer;
import com.example.accountservice.model.UserRegistrationDTO;
import com.example.accountservice.repository.CustomerRepository;
import com.example.accountservice.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class CustomerController {

    private final RestTemplate restTemplate;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private TransferRepository transferRepository;

    public CustomerController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    /*@GetMapping("/customers/{customerId}/transfers")
    public List<Transfer> getAllTransfersForCustomer(@PathVariable Long customerId) {
        return transferRepository.findAllBySenderId(customerId);
    }*/

    @PostMapping("/customer")
    Customer newCustomer(@RequestBody Customer newCustomer) {
        Customer savedCustomer = customerRepository.save(newCustomer);

        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setFirstName(newCustomer.getFirstName());
        userRegistrationDTO.setFirstName(newCustomer.getLastName());

        userRegistrationDTO.setCustomerId(newCustomer.getCustomerId());
        String authRegisterUrl = "http://localhost:8888/AUTH-SERVICE/auth/register";
        ResponseEntity<Void> authResponse = restTemplate.postForEntity(authRegisterUrl, userRegistrationDTO, Void.class);


        return savedCustomer;
    }




    @GetMapping("/customers")
    List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping("/customer/{CustomerId}")
    Customer getCustomerById(@PathVariable Long CustomerId) {
        return customerRepository.findById(CustomerId)
                .orElseThrow(() -> new CustomerNotFoundException(CustomerId));
    }

    @PutMapping("/customer/{customerId}")
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


    @DeleteMapping("/customer/{CustomerId}")
    String deleteCustomer(@PathVariable Long CustomerId){
        if(!customerRepository.existsById(CustomerId)){
            throw new CustomerNotFoundException(CustomerId);
        }
        customerRepository.deleteById(CustomerId);
        return  "Customer with id "+CustomerId+" has been deleted success.";
    }

}
