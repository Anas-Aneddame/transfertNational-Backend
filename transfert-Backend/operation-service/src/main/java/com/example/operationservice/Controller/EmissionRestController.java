package com.example.operationservice.Controller;

import com.example.operationservice.Reponse.EmissionResponse;
import com.example.operationservice.Enum.FraisType;
import com.example.operationservice.Enum.OperationType;
import com.example.operationservice.Enum.TransferStatus;
import com.example.operationservice.Model.Beneficiary;
import com.example.operationservice.Model.Customer;
import com.example.operationservice.Model.Operation;
import com.example.operationservice.Model.Transfer;
import com.example.operationservice.Reponse.OtpResponse;
import com.example.operationservice.Repository.BeneficiaryRepository;
import com.example.operationservice.Repository.CustomerRepository;
import com.example.operationservice.Repository.OperationRepository;
import com.example.operationservice.Repository.TransferRepository;
import com.example.operationservice.Request.OperationBody;
import com.example.operationservice.Request.OtpRequest;
import com.example.operationservice.Request.TransferBody;
import com.example.operationservice.Request.TransferRequest;
import com.example.operationservice.Service.EmailSenderService;
import com.example.operationservice.Service.RandomPasswordGenerator;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Random;

@RestController
@CrossOrigin("http://localhost:4200/")
@RequestMapping("/operation")
public class EmissionRestController {
    TransferRepository transferRepository;
    CustomerRepository customerRepository;
    BeneficiaryRepository beneficiaryRepository;
    RandomPasswordGenerator randomPasswordGenerator;
    EmailSenderService senderService;
    OperationRepository operationRepository;

    public EmissionRestController(TransferRepository transferRepository,CustomerRepository customerRepository
            ,BeneficiaryRepository beneficiaryRepository,OperationRepository operationRepository,
                                  EmailSenderService senderService, RandomPasswordGenerator randomPasswordGenerator)
    {
        this.transferRepository=transferRepository;
        this.customerRepository=customerRepository;
        this.beneficiaryRepository=beneficiaryRepository;
        this.operationRepository=operationRepository;
        this.senderService = senderService;
        this.randomPasswordGenerator=randomPasswordGenerator;
    }
    @PostMapping("/emission")
    public EmissionResponse emettreTransfer(@RequestBody TransferRequest transferRequest)
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
                return EmissionResponse.builder().msg("Dépassse le plafond de 80000").build();
            }
        }
        else{
            if(transferBody.getAmount()>2000)
            {
                return  EmissionResponse.builder().msg("Dépasse le plafond de 2000").build();

            } else if (transferBody.getAmount()>customer.get().getBalance()) {
                return  EmissionResponse.builder().msg("Dépasse le solde de compte de paiement du client").build();

            } else if (transferBody.getAmount()>customer.get().getPlafondAnnuel()) {
                return EmissionResponse.builder().msg("Dépasse le plafond annuel").build();

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
        String otp = randomPasswordGenerator.generatePassayPassword();
        System.out.println(otp);
        Transfer transfer = Transfer.builder()
                .transferReference(transferReference)
                .status(TransferStatus.A_SERVIR)
                .amount(transferAmount)
                .sender(customer.get())
                .receiver(beneficiary.get())
                .otp(otp)
                .confirmed(false)
                .build();

        Operation operation = Operation.builder()
                .operationType(operationBody.getOperationType())
                .transferType(operationBody.getTransferType())
                .transferReference(transfer)
                .operationType(operationBody.getOperationType())
                .build();

        Customer newCustomer = Customer.builder().
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
        senderService.sendSimpleEmail(customer.get().getEmail(),"OTP verification",otp);

        customerRepository.save(newCustomer);
        transferRepository.save(transfer);
        operationRepository.save(operation);

        return  EmissionResponse.builder().transferRef(transfer.getTransferReference()).build();

    }

    @GetMapping("/create")
    public void create()
    {
//        Customer c = Customer.builder()
//                .FirstName("Jonathan")
//                .LastName("Joestar")
//                .Email("nizar.bouhsaine@gmail.com")
//                .Balance(20000.0)
//                .Phone("0607080910")
//                .plafondAnnuel(20000.0).build();
//        customerRepository.save(c);

        Customer cc = customerRepository.getById(1L);
        Beneficiary b = Beneficiary.builder().firstName("Jack").lastName("Sparrow").email("Jack@sparrow.com").phone("0607080910").customer(cc).build();
        Beneficiary b2 = Beneficiary.builder().firstName("Agatha").lastName("Christie").email("Agatha@Christie.com").phone("067481245").customer(cc).build();

        beneficiaryRepository.save(b);
        beneficiaryRepository.save(b2);


    }

    @PostMapping("/emissionotp")
    public OtpResponse verifyOtp(@RequestBody OtpRequest otpRequest)
    {
        Transfer transfer = transferRepository.getReferenceById(otpRequest.getTransferRef());
        if(transfer.getOtp().matches(otpRequest.getOtp()))
        {
            transfer.setConfirmed(true);
            transferRepository.save(transfer);
            return OtpResponse.builder().msg("OTP verified successfully !").build();
        }
        else {
            return OtpResponse.builder().msg("OTP not verified !").build();
        }
    }
    @PostMapping("/cancel")
    public void cancelTransfer()
    {

    }

}
