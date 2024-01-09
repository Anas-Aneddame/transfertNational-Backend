package com.example.operationservice.Controller;

import com.example.operationservice.DTO.TransferDTO;
import com.example.operationservice.Enum.TransferStatus;
import com.example.operationservice.Model.Beneficiary;
import com.example.operationservice.Model.Customer;
import com.example.operationservice.Model.Transfer;
import com.example.operationservice.Reponse.TransferBackofficeResponse;
import com.example.operationservice.Repository.BeneficiaryRepository;
import com.example.operationservice.Repository.CustomerRepository;
import com.example.operationservice.Repository.TransferRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/transfers")
@CrossOrigin("*")
public class TransferRestController {
    private TransferRepository transferRepository;
    private CustomerRepository customerRepository;
    private BeneficiaryRepository beneficiaryRepository;

    public TransferRestController(TransferRepository transferRepository,CustomerRepository customerRepository,BeneficiaryRepository beneficiaryRepository) {
        this.transferRepository = transferRepository;
        this.customerRepository=customerRepository;
        this.beneficiaryRepository=beneficiaryRepository;
    }

    @GetMapping("/data")
    public String data(){
        Customer customer=
                Customer.builder()
                        .Email("hamdanimee@gmail.com")
                        .FirstName("Mohamed")
                        .LastName("Hamdani")
                        .Balance(10000D)
                        .plafondAnnuel(20000D)
                        .build();
        customerRepository.save(customer);
        Customer customer2=
                Customer.builder()
                        .Email("hamdanimee@gmail.com")
                        .FirstName("Brahim")
                        .LastName("Sadik")
                        .Balance(12000D)
                        .plafondAnnuel(20000D)
                        .build();
        customerRepository.save(customer2);

        Beneficiary beneficiary=Beneficiary.builder().firstName("Jane").lastName("Justen").email("Jane@austen.com").phone("0607098091").build();
        beneficiaryRepository.save(beneficiary);

        Beneficiary beneficiary2=Beneficiary.builder().firstName("Jack").lastName("Hollow").email("justinHollow@ensa.com").phone("0611098091").build();
        beneficiaryRepository.save(beneficiary2);

        transferRepository.saveAll(List.of(
                Transfer.builder()
                        .transferReference("001")
                        .amount(2000)
                        .confirmed(true)
                        .sender(customer)
                        .receiver(beneficiary)
                        .status(TransferStatus.A_SERVIR)
                        .timestamp(System.currentTimeMillis())
                        .build(),
                Transfer.builder()
                        .transferReference("002")
                        .amount(1000)
                        .confirmed(true)
                        .sender(customer2)
                        .receiver(beneficiary2)
                        .status(TransferStatus.A_SERVIR)
                        .timestamp(System.currentTimeMillis())
                        .build()
        ));
        return "added";
    }

    @GetMapping("/all")
    public List<TransferDTO> getAllTransfers(){
        List<Transfer> transferList= transferRepository.findAll();
        List<TransferDTO> transferDTOS=new ArrayList<>();
        transferList.forEach(transfer -> {
            transferDTOS.add(TransferDTO.builder()
                    .transferReference(transfer.getTransferReference())
                    .receiver(transfer.getReceiver().getId())
                    .sender(transfer.getSender().getCustomerId())
                    .motif(transfer.getMotif())
                    .amount(transfer.getAmount())
                    .codepin(transfer.getCodepin())
                    .otp(transfer.getOtp())
                    .confirmed(transfer.isConfirmed())
                    .timestamp(transfer.getTimestamp())
                    .status(transfer.getStatus())
                    .build());
        });

        return transferDTOS;
    }
    @GetMapping("/{reference}")
    public TransferBackofficeResponse getTransferByRef(@PathVariable String reference){
        System.out.println(reference);
        Transfer transfer=transferRepository.findTransferByTransferReference(reference);
        TransferDTO transferDTO=TransferDTO.builder()
                .status(transfer.getStatus())
                .motif(transfer.getMotif())
                .transferReference(transfer.getTransferReference())
                .receiver(transfer.getReceiver().getId())
                .sender(transfer.getSender().getCustomerId())
                .timestamp(transfer.getTimestamp())
                .confirmed(transfer.isConfirmed())
                .amount(transfer.getAmount())
                .otp(transfer.getOtp())
                .codepin(transfer.getCodepin())
                .build();
        return TransferBackofficeResponse.builder()
                .transferDTO(transferDTO)
                .beneficiary(transfer.getReceiver())
                .customer(transfer.getSender())
                .build();
    }
}
