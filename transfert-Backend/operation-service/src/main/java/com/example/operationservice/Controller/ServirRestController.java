package com.example.operationservice.Controller;

import com.example.operationservice.Enum.OperationType;
import com.example.operationservice.Enum.TransferStatus;
import com.example.operationservice.Enum.TransferType;
import com.example.operationservice.Model.Beneficiary;
import com.example.operationservice.Model.Customer;
import com.example.operationservice.Model.Operation;
import com.example.operationservice.Model.Transfer;
import com.example.operationservice.Reponse.ServirResponse;
import com.example.operationservice.Repository.BeneficiaryRepository;
import com.example.operationservice.Repository.CustomerRepository;
import com.example.operationservice.Repository.OperationRepository;
import com.example.operationservice.Repository.TransferRepository;
import com.example.operationservice.Request.ServirBody;
import com.example.operationservice.Request.ServirOtpRequest;
import com.example.operationservice.Service.EmailSenderService;
import com.example.operationservice.Service.RandomPasswordGenerator;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;

@RestController
@RequestMapping("/servir")
public class ServirRestController {
    private TransferRepository transferRepository;
    private CustomerRepository customerRepository;
    private BeneficiaryRepository beneficiaryRepository;
    private OperationRepository operationRepository;
    private EmailSenderService emailSenderService;
    private RandomPasswordGenerator randomPasswordGenerator;

    public ServirRestController(TransferRepository transferRepository, CustomerRepository customerRepository, BeneficiaryRepository beneficiaryRepository, OperationRepository operationRepository, EmailSenderService emailSenderService, RandomPasswordGenerator randomPasswordGenerator){
        this.transferRepository=transferRepository;
        this.customerRepository=customerRepository;
        this.operationRepository=operationRepository;
        this.emailSenderService=emailSenderService;
        this.beneficiaryRepository=beneficiaryRepository;
        this.randomPasswordGenerator=randomPasswordGenerator;

    }

    @GetMapping("/data")
    public void addData(){
        Customer customer=new Customer().builder()
                .CNE("J556222")
                .Email("hamdanimee@gmail.com")
                .Phone("0676158831")
                .Balance(20000D)
                .plafondAnnuel(20000D)
                .FirstName("Mohamed")
                .LastName("Hamdani")
                .build();
        customerRepository.save(customer);
        Beneficiary beneficiary=new Beneficiary().builder()
                .phone("07777777")
                .firstName("Brahim")
                .lastName("briming")
                .email("hamdanimee@gmail.com")
                .build();
        beneficiaryRepository.save(beneficiary);
        Transfer transfer=new Transfer().builder()
                .transferReference("001")
                .amount(2000D)
                .sender(customer)
                .receiver(beneficiary)
                .status(TransferStatus.A_SERVIR)
                .confirmed(true)
                .codepin("99999")
                .timestamp(System.currentTimeMillis())
                .build();
        transferRepository.save(transfer);
        Operation operation=new Operation().builder()
                .agentId(86L)
                .transferReference(transfer)
                .operationType(OperationType.ESPECE_CONSOLE_AGENT)
                .transferType(TransferType.EMISSION)
                .timestamp(System.currentTimeMillis())
                .build();
        operationRepository.save(operation);
    }

    @PostMapping("/espece-console")
    public ServirResponse servieEspeceConsoleAgent(@RequestBody ServirBody servirBody){
        Beneficiary beneficiary=beneficiaryRepository.findById(servirBody.getBeneficiaryId()).orElse(null);

        String transferReference=servirBody.getTransferReference();
        Transfer transfer=transferRepository.findById(transferReference).orElse(null);

        if(beneficiary == null){
            return new ServirResponse().builder().msg("Message Bloquant, Beneficairy n'existe pas").build();
        }
        if(transfer == null){
            return new ServirResponse().builder().msg("Message Bloquant , transfer n'existe pas").build();
        }
        if(!transfer.isConfirmed()){
            return new ServirResponse().builder().msg("Message Bloquant , transfer n'est pas confirmé").build();
        }
        if(transfer.getStatus()== TransferStatus.SERVIE) {
            return new ServirResponse().builder().msg("Message Bloquant , transfer deja paye").build();
        }
        if(transfer.getStatus() != TransferStatus.A_SERVIR){
            return new ServirResponse().builder().msg("Message Bloquant , transfer pas dans l'etat a_servir").build();
        }
        //client blacklist dans sirone

        String otp=randomPasswordGenerator.generatePassayPassword();
        transfer.setOtp(otp);
        transferRepository.save(transfer);
        emailSenderService.sendSimpleEmail(beneficiary.getEmail(),"OTP","Code : "+otp);

        return new ServirResponse().builder().msg("Demandez OTP de beneficiare").build();
    }
    @PostMapping("/espece-console/verify-otp")
    public ServirResponse servirEspeceConsoleAgentVerifyOtp(@RequestBody ServirOtpRequest servirOtpRequest){
        Long agentId= servirOtpRequest.getAgentId();

        String transferReference=servirOtpRequest.getTransferRef();
        Transfer transfer=transferRepository.findById(transferReference).orElse(null);

        if(transfer == null){
            return new ServirResponse().builder().msg("Message Bloquant , transfer n'existe pas").build();
        }
        if(!transfer.isConfirmed()){
            return new ServirResponse().builder().msg("Message Bloquant , transfer n'est pas confirmé").build();
        }
        if(!transfer.getOtp().equals(servirOtpRequest.getOtp())){
            return new ServirResponse().builder().msg("Wrong OTP").build();
        }

        //wipe transfer otp
        transfer.setOtp(null);

        //Add emission operation
        Operation operation=Operation.builder()
                .operationType(OperationType.ESPECE_CONSOLE_AGENT)
                .transferType(TransferType.EMISSION)
                .agentId(agentId)
                .timestamp(System.currentTimeMillis())
                .transferReference(transfer)
                .build();
        operationRepository.save(operation);

        //update transfer status
        transfer.setStatus(TransferStatus.SERVIE);
        transferRepository.save(transfer);

        return new ServirResponse().builder().msg("Confirmer reception d'argent , edition du recu").build();
    }

    @PostMapping("/wallet")
    public ServirResponse servieWalletConsoleAgent(@RequestBody ServirBody servirBody){
        Beneficiary beneficiary=beneficiaryRepository.findById(servirBody.getBeneficiaryId()).orElse(null);
        Customer wallet=customerRepository.findByEmail(beneficiary.getEmail());

        String transferReference=servirBody.getTransferReference();
        Transfer transfer=transferRepository.findById(transferReference).orElse(null);

        if(beneficiary == null){
            return new ServirResponse().builder().msg("Message Bloquant, Beneficairy n'existe pas").build();
        }
        if(wallet == null){
            return new ServirResponse().builder().msg("Message Bloquant, Beneficiary n'a pas de wallet").build();
        }
        if(transfer == null){
            return new ServirResponse().builder().msg("Message Bloquant , transfer n'existe pas").build();
        }
        if(!transfer.isConfirmed()){
            return new ServirResponse().builder().msg("Message Bloquant , transfer n'est pas confirmé").build();
        }
        if(transfer.getStatus()== TransferStatus.SERVIE) {
            return new ServirResponse().builder().msg("Message Bloquant , transfer deja paye").build();
        }
        if(transfer.getStatus() != TransferStatus.A_SERVIR){
            return new ServirResponse().builder().msg("Message Bloquant , transfer pas dans l'etat a_servir").build();
        }
        //client blacklist dans sirone

        String otp=randomPasswordGenerator.generatePassayPassword();
        transfer.setOtp(otp);
        transferRepository.save(transfer);
        emailSenderService.sendSimpleEmail(beneficiary.getEmail(),"OTP","Code : "+otp);

        return new ServirResponse().builder().msg("Demandez OTP de beneficiare").build();
    }
    @PostMapping("/wallet/verify-otp")
    public ServirResponse servieWalletConsoleAgentVerifyOtp(@RequestBody ServirOtpRequest servirOtpRequest){
        Long agentId= servirOtpRequest.getAgentId();

        String transferReference=servirOtpRequest.getTransferRef();
        Transfer transfer=transferRepository.findById(transferReference).orElse(null);

        if(transfer == null){
            return new ServirResponse().builder().msg("Message Bloquant , transfer n'existe pas").build();
        }
        if(!transfer.isConfirmed()){
            return new ServirResponse().builder().msg("Message Bloquant , transfer n'est pas confirmé").build();
        }
        if(servirOtpRequest.getOtp() == null || !transfer.getOtp().equals(servirOtpRequest.getOtp())){
            return new ServirResponse().builder().msg("Wrong OTP").build();
        }

        Customer wallet=customerRepository.findByEmail(transfer.getReceiver().getEmail());
        if(wallet ==null){
            return new ServirResponse().builder().msg("Message Bloquant , wallet n'existe pas").build();
        }

        //wipe transfer otp
        transfer.setOtp(null);

        //Add emission operation
        Operation operation=Operation.builder()
                .operationType(OperationType.WALLET_CONSOLE_AGENT)
                .transferType(TransferType.EMISSION)
                .agentId(agentId)
                .timestamp(System.currentTimeMillis())
                .transferReference(transfer)
                .build();
        operationRepository.save(operation);

        //update transfer status
        transfer.setStatus(TransferStatus.SERVIE);
        transferRepository.save(transfer);

        //add solde to beneficiary wallet (customer)
        wallet.setBalance(wallet.getBalance()+transfer.getAmount());
        customerRepository.save(wallet);

        return new ServirResponse().builder().msg("Confirmer reception d'argent , edition du recu").build();
    }

    @PostMapping("/GAB")
    public ServirResponse servieEspeceGAB(@RequestBody ServirBody servirBody){
        Long agentId=servirBody.getAgentId();

        Beneficiary beneficiary=beneficiaryRepository.findById(servirBody.getBeneficiaryId()).orElse(null);

        String transferReference=servirBody.getTransferReference();
        Transfer transfer=transferRepository.findById(transferReference).orElse(null);
        if(beneficiary == null){
            return new ServirResponse().builder().msg("Message Bloquant, Beneficairy n'existe pas").build();
        }
        if(transfer == null){
            return new ServirResponse().builder().msg("Message Bloquant , transfer n'existe pas").build();
        }
        if(!transfer.isConfirmed()){
            return new ServirResponse().builder().msg("Message Bloquant , transfer n'est pas confirmé").build();
        }
        if(transfer.getStatus()== TransferStatus.SERVIE) {
            return new ServirResponse().builder().msg("Message Bloquant , transfer deja paye").build();
        }
        if(transfer.getStatus() != TransferStatus.A_SERVIR){
            return new ServirResponse().builder().msg("Message Bloquant , transfer pas dans l'etat a_servir").build();
        }
        if(servirBody.getCodepin()==null || !servirBody.getCodepin().equals(transfer.getCodepin())){
            return new ServirResponse().builder().msg("Wrong Code").build();
        }

        //Add emission operation
        Operation operation=Operation.builder()
                .operationType(OperationType.ESPECE_GAB)
                .transferType(TransferType.EMISSION)
                .agentId(agentId)
                .timestamp(System.currentTimeMillis())
                .transferReference(transfer)
                .build();
        operationRepository.save(operation);

        //update transfer status
        transfer.setStatus(TransferStatus.SERVIE);
        transferRepository.save(transfer);

        return new ServirResponse().builder().msg("Confirmer reception d'argent , Edition du recu").build();
    }
}
