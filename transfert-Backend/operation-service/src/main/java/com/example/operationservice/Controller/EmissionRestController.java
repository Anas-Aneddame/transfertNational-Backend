package com.example.operationservice.Controller;

import com.example.operationservice.Enum.FraisType;
import com.example.operationservice.Enum.OperationType;
import com.example.operationservice.Enum.TransferStatus;
import com.example.operationservice.Enum.TransferType;
import com.example.operationservice.Model.Beneficiary;
import com.example.operationservice.Model.Customer;
import com.example.operationservice.Model.Operation;
import com.example.operationservice.Model.Transfer;
import com.example.operationservice.Repository.BeneficiaryRepository;
import com.example.operationservice.Repository.CustomerRepository;
import com.example.operationservice.Repository.OperationRepository;
import com.example.operationservice.Repository.TransferRepository;
import com.example.operationservice.Request.OperationBody;
import com.example.operationservice.Request.TransferBody;
import com.example.operationservice.Request.TransferRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/OPERATION-SERVICE")
public class EmissionRestController {
    TransferRepository transferRepository;
    CustomerRepository customerRepository;
    BeneficiaryRepository beneficiaryRepository;

    OperationRepository operationRepository;

    public EmissionRestController(TransferRepository transferRepository,CustomerRepository customerRepository,BeneficiaryRepository beneficiaryRepository,OperationRepository operationRepository){this.transferRepository=transferRepository;this.customerRepository=customerRepository;this.beneficiaryRepository=beneficiaryRepository;this.operationRepository=operationRepository;}
    @PostMapping("/Emission")
    public ResponseEntity<String>  emettreTransfer(@RequestBody TransferRequest transferRequest)
    {

        TransferBody transferBody = transferRequest.getTransferBody();
        OperationBody operationBody =  transferRequest.getOperationBody();
        long senderId = transferBody.getSenderId();
        Optional<Customer> customer = customerRepository.findById(transferBody.getSenderId());
        Optional<Beneficiary> beneficiary = beneficiaryRepository.findById(transferBody.getReceiverId());

        if(operationBody.getOperationType()== OperationType.ESPECE_CONSOLE_AGENT)
        {
            if(transferBody.getAmount()>80000)
            {
                return ResponseEntity.ok().body("Dépasse 80000");
            }
        }
        else{
            if(transferBody.getAmount()>2000)
            {
                return  ResponseEntity.ok().body("Dépasse le plafond de 2000");

            } else if (transferBody.getAmount()>customer.get().getBalance()) {
                return  ResponseEntity.ok().body("Dépasse le solde de compte de paiement du client");

            } else if (transferBody.getAmount()>customer.get().getPlafondAnnuel()) {
                return ResponseEntity.ok().body("Dépasse le plafond annuel");

            }
        }

        double transferAmount= transferBody.getAmount();
        double deductAmount = transferBody.getAmount();
        double frais = 50.0;
        if(transferRequest.getFraisType()== FraisType.DONOR)
        {
            deductAmount=deductAmount+frais;
            System.out.println(deductAmount);

        } else if (transferRequest.getFraisType()== FraisType.BENEFICIARY) {
            transferAmount=transferAmount-frais;
        }
        else{
            deductAmount=deductAmount+0.5*frais;
            transferAmount=transferAmount-0.5*frais;
        }

        Random rnd = new Random();
        int n = 1000000 + rnd.nextInt(9000000);
        String transferReference = "EDP837"+n;

        Transfer transfer = Transfer.builder()
                .transferReference(transferReference)
                .status(TransferStatus.A_SERVIR)
                .amount(transferAmount)
                        .sender(customer.get())
                                .receiver(beneficiary.get()).build();

        Operation operation = Operation.builder()
                .operationType(operationBody.getOperationType())
                .transferType(operationBody.getTransferType()).
                transferReference(transfer).operationType(operationBody.getOperationType())
                .build();

        Customer customer1 = Customer.builder().
                CustomerId(transferBody.getSenderId())
                .FirstName(customer.get().getFirstName())
                .LastName(customer.get().getLastName())
                .Balance(customer.get().getBalance()-deductAmount)
                .Phone(customer.get().getPhone())
                .plafondAnnuel(customer.get().getPlafondAnnuel()-deductAmount)
                .CNE(customer.get().getCNE())
                .AccountType(customer.get().getAccountType())
                .Email(customer.get().getEmail())
                .build();

        customerRepository.save(customer1);
        transferRepository.save(transfer);
        operationRepository.save(operation);

        return  ResponseEntity.ok().body("Transfert ajouté en attendant l'otp");

    }

    @GetMapping("/create")
    public void getcreate()
    {
        Customer c = Customer.builder().FirstName("Jonathan").LastName("Joestar").Email("jonathan@joestar.com").Balance(20000.0).Phone("0607080910").plafondAnnuel(20000.0).build();
        customerRepository.save(c);
//        operationRepository.deleteAll();
//        transferRepository.deleteAll();
//        beneficiaryRepository.deleteAll();
        Beneficiary b = Beneficiary.builder().firstName("Jane").lastName("Austen").email("Jane@austen.com").phone("0607098091").build();
        beneficiaryRepository.save(b);
    }
}
