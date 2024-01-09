package com.example.accountservice.controller;

import com.example.accountservice.exception.CustomerNotFoundException;
import com.example.accountservice.model.Customer;
import com.example.accountservice.model.Transfer;
import com.example.accountservice.model.UserRegistrationDTO;
import com.example.accountservice.repository.CustomerRepository;
import com.example.accountservice.repository.TransferRepository;
import com.example.accountservice.service.EmailSenderService;
import com.example.accountservice.service.PasswordGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/customers")
public class CustomerController {

    private final RestTemplate restTemplate;
    @Autowired
    PasswordGeneratorService passwordGeneratorService;

    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private TransferRepository transferRepository;

    public CustomerController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @GetMapping("/{customerId}/transfers")
    public ResponseEntity<List<Transfer>> getTransfersByCustomer(@PathVariable Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        List<Transfer> transfers = transferRepository.findAllBySender(customer);
        return new ResponseEntity<>(transfers, HttpStatus.OK);
    }

    @PostMapping("/add-customer")
    Customer newCustomer(@RequestBody Customer newCustomer) {
        // Ajout du Customer
        Customer savedCustomer = customerRepository.save(newCustomer);

        //objet DTO pour l'enregistrement de l'utilisateur
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setFirstName(newCustomer.getFirstName());
        userRegistrationDTO.setLastName(newCustomer.getLastName());
        userRegistrationDTO.setCustomerId(newCustomer.getCustomerId());
        userRegistrationDTO.setPassword(passwordGeneratorService.generatePassword());

        String authRegisterUrl = "http://localhost:8888/AUTH-SERVICE/auth/register";
        ResponseEntity<Void> authResponse = restTemplate.postForEntity(authRegisterUrl, userRegistrationDTO, Void.class);
        emailSenderService.sendSimpleEmail(newCustomer.getEmail(),"Nouveau mot de passe pour votre compte de l'application de transfert","Nous vous informons que votre mot de passe pour l'application de transfert a été réinitialisé avec succès. Vous trouverez ci-dessous vos nouvelles informations d'identification :\n password :  "+userRegistrationDTO.getPassword());

        return savedCustomer;
    }




    @GetMapping("/all-customers")
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
