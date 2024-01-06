package com.example.operationservice.Controller;

import com.example.operationservice.Enum.OperationType;
import com.example.operationservice.Enum.TransferStatus;
import com.example.operationservice.Enum.TransferType;
import com.example.operationservice.Model.Beneficiary;
import com.example.operationservice.Model.Customer;
import com.example.operationservice.Model.Operation;
import com.example.operationservice.Model.Transfer;
import com.example.operationservice.Repository.CustomerRepository;
import com.example.operationservice.Repository.OperationRepository;
import com.example.operationservice.Repository.TransferRepository;
import com.example.operationservice.Service.EmailSenderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/servie")
public class ServirController {
    private TransferRepository transferRepository;
    private CustomerRepository customerRepository;
    private OperationRepository operationRepository;
    private EmailSenderService emailSenderService;

    public ServirController(TransferRepository transferRepository,CustomerRepository customerRepository,OperationRepository operationRepository,EmailSenderService emailSenderService){
        this.transferRepository=transferRepository;
        this.customerRepository=customerRepository;
        this.operationRepository=operationRepository;
        this.emailSenderService=emailSenderService;
    }


    public void servieEspeceConsoleAgent(String transferReference,Long agentId){
        Transfer transfer=transferRepository.findById(transferReference).orElse(null);
//        Customer customer=new Customer();
//        Beneficiary beneficiary=new Beneficiary();

        if(transfer == null){
            System.out.println("Message Bloquant , transfer n'existe pas");
//            return ;
        }
        if(transfer.getStatus()== TransferStatus.SERVIE) {
            System.out.println("Message Bloquant , transfer deja paye");
//            return;
        }
        if(transfer.getStatus() != TransferStatus.A_SERVIR){
            System.out.println("Message Bloquant , transfer pas dans l'etat a_servir");
//            return ;
        }
        //client blacklist dans sirone

//        customer=transfer.getSender();
//        beneficiary=transfer.getReceiver();

        emailSenderService.sendSimpleEmail("hamdanimee@gmail.com","OTP","Code : 98k34jl");

        //Add emission operation
//        Operation operation=Operation.builder()
//                .operationType(OperationType.ESPECE_CONSOLE_AGENT)
//                .transferType(TransferType.EMISSION)
//                .agentId(agentId)
//                .timestamp(System.currentTimeMillis())
//                .transferReference(transfer)
//                .build();
//        operationRepository.save(operation);
//        //update transfer status
//        transfer.setStatus(TransferStatus.SERVIE);
//        transferRepository.save(transfer);

        System.out.println("Confirmer reception d'argent , edition du recu");

    }
    public void servieWalletConsoleAgent(String transferReference,Long agentId){
        Transfer transfer=transferRepository.findById(transferReference).orElse(null);
//        Customer customer=new Customer();
//        Beneficiary beneficiary=new Beneficiary();

        if(transfer == null){
            System.out.println("Message Bloquant , transfer n'existe pas");
            return ;
        }
        if(transfer.getStatus()== TransferStatus.SERVIE){
            System.out.println("Message Bloquant , transfer deja paye");
            return ;
        }
        if(transfer.getStatus() != TransferStatus.A_SERVIR){
            System.out.println("Message Bloquant , transfer pas dans l'etat a_servir");
            return ;
        }
        //client blacklist dans sirone

//        customer=transfer.getSender();
//        beneficiary=transfer.getReceiver();



        transfer.setStatus(TransferStatus.SERVIE);
        transferRepository.save(transfer);
        System.out.println("Confirmer reception d'argent , edition du recu");
    }
    public void servieEspeceGAB(){
        //transfer dans l'etat 'a servir' ou etat 'deploque a servir' + j meme jour de debloquage
    }
}
