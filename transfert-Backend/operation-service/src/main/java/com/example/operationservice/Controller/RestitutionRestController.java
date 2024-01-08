package com.example.operationservice.Controller;

import com.example.operationservice.Enum.OperationType;
import com.example.operationservice.Enum.TransferStatus;
import com.example.operationservice.Model.Customer;
import com.example.operationservice.Model.Transfer;
import com.example.operationservice.Reponse.EmissionResponse;
import com.example.operationservice.Reponse.OtpResponse;
import com.example.operationservice.Repository.CustomerRepository;
import com.example.operationservice.Repository.TransferRepository;
import com.example.operationservice.Request.OtpRequest;
import com.example.operationservice.Request.RestitutionRequest;
import com.example.operationservice.Service.EmailSenderService;
import com.example.operationservice.Service.RandomPasswordGenerator;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/OPERATION-SERVICE")
public class RestitutionRestController {

    TransferRepository transferRepository;
    CustomerRepository customerRepository;
    RandomPasswordGenerator randomPasswordGenerator;
    EmailSenderService senderService;

    public RestitutionRestController(TransferRepository transferRepository,
                                     CustomerRepository customerRepository,
                                     RandomPasswordGenerator randomPasswordGenerator,
                                     EmailSenderService senderService
    )
    {
        this.transferRepository = transferRepository;
        this.customerRepository = customerRepository;
        this.randomPasswordGenerator = randomPasswordGenerator;
        this.senderService = senderService;
    }

    @PostMapping("/restitution")
    public EmissionResponse restituer(@RequestBody RestitutionRequest restitutionRequest)
    {
        Transfer transfer = transferRepository.getReferenceById(restitutionRequest.getTransferReference());
        Customer customer = customerRepository.getById(transfer.getSender().getCustomerId());

        if(restitutionRequest.getOperationType()== OperationType.ESPECE_CONSOLE_AGENT)
        {
            transfer.setStatus(TransferStatus.RESTITUE);
            customer.setBalance(customer.getBalance()+transfer.getAmount());
            transfer.setMotif(restitutionRequest.getMotif());
            customerRepository.save(customer);
            transferRepository.save(transfer);
            return EmissionResponse.builder().msg("Restitution avec succés !!").build();
        }
        else{
            String otp = randomPasswordGenerator.generatePassayPassword();
            transfer.setOtp(otp);
            System.out.println(otp);
            transferRepository.save(transfer);
            senderService.sendSimpleEmail(customer.getEmail(),"OTP verification",otp);
            return EmissionResponse.builder().transferRef(transfer.getTransferReference()).build();
        }
    }

    @PostMapping("/restituerotp")
    public OtpResponse verifierotp(@RequestBody OtpRequest otpRequest)
    {

        Transfer transfer = transferRepository.getReferenceById(otpRequest.getTransferRef());
        Customer customer = customerRepository.getById(transfer.getSender().getCustomerId());
        if(transfer.getOtp().matches(otpRequest.getOtp()))
        {
            transfer.setStatus(TransferStatus.RESTITUE);
            customer.setBalance(customer.getBalance()+transfer.getAmount());
            customerRepository.save(customer);
            transferRepository.save(transfer);
            return OtpResponse.builder().msg("OTP verified successfully !").build();
        }
        else {
            return OtpResponse.builder().msg("OTP not verified !").build();
        }
    }
}
