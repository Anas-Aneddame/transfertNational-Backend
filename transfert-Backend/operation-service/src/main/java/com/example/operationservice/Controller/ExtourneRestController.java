package com.example.operationservice.Controller;

import com.example.operationservice.Enum.TransferStatus;
import com.example.operationservice.Enum.TransferType;
import com.example.operationservice.Model.Operation;
import com.example.operationservice.Model.Transfer;
import com.example.operationservice.Reponse.EmissionResponse;
import com.example.operationservice.Reponse.ServirResponse;
import com.example.operationservice.Repository.OperationRepository;
import com.example.operationservice.Repository.TransferRepository;
import com.example.operationservice.Request.ExtourneRequest;
import com.example.operationservice.Request.ServirBody;
import org.springframework.cglib.core.Local;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/")
public class ExtourneRestController {
    private TransferRepository transferRepository;
    private OperationRepository operationRepository;

    public ExtourneRestController(TransferRepository transferRepository, OperationRepository operationRepository) {
        this.transferRepository = transferRepository;
        this.operationRepository = operationRepository;
    }

    @PostMapping("/extourne")
    public ServirResponse extourne(@RequestBody ExtourneRequest extourneBody){
        Transfer transfer=transferRepository.findById(extourneBody.getTransferReference()).orElse(null);

        if(transfer == null){
            return new ServirResponse().builder().msg("Message Bloquant , transfer n'existe pas").build();
        }
        if(transfer.getStatus() != TransferStatus.A_SERVIR){
            return new ServirResponse().builder().msg("Message Bloquant , transfer pas dans l'etat a_servir").build();
        }
        if(transfer.getStatus()== TransferStatus.SERVIE) {
            return new ServirResponse().builder().msg("Message Bloquant , transfer deja paye").build();
        }
        if(transfer.getStatus()== TransferStatus.BLOQUE) {
            return new ServirResponse().builder().msg("Message Bloquant , transfer est bloque").build();
        }

        List<Operation> operationList=operationRepository.findAllByTransferReferenceAndTransferType(transfer.getTransferReference(),TransferType.EMISSION);

        if(operationList.size()==0){
            return new ServirResponse().builder().msg("le transfer n'est pas emi").build();
        }
        if(operationList.get(0).getAgentId() != extourneBody.getAgentId()){
            return new ServirResponse().builder().msg("le transfer n'est pas ete emitter par le meme agent").build();
        }

        LocalDateTime timestampOperation=LocalDateTime.ofInstant(Instant.ofEpochMilli(operationList.get(0).getTimestamp()),ZoneId.systemDefault());
        LocalDate dateOperation=timestampOperation.toLocalDate();
        if(!dateOperation.isEqual(LocalDate.now())){
            return new ServirResponse().builder().msg("Le transfert n'est pas emi au meme jour").build();
        }

        transfer.setStatus(TransferStatus.EXTOURNE);
        transfer.setMotif(extourneBody.getMotif());
        transferRepository.save(transfer);

        return new ServirResponse().builder().msg("Extourner avec succés !!").build();

    }
}
