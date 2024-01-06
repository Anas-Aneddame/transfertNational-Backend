package com.example.accountservice.controller;

import com.example.accountservice.exception.TransferNotFoundException;
import com.example.accountservice.model.Operation;
import com.example.accountservice.model.Transfer;
import com.example.accountservice.repository.OperationRepository;
import com.example.accountservice.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class TransferController {

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private OperationRepository operationRepository;

    @GetMapping("/getTransfersForCustomer/{customerId}")
    public List<Transfer> getTransfersForCustomer(@PathVariable Long customerId) {
        return transferRepository.findAllBySenderIdOrReceiverId(customerId, customerId);
    }

    @GetMapping("/{transferReference}/operations")
    public List<Operation> getOperationsForTransfer(@PathVariable Long transferReference) {
        Transfer transfer = transferRepository.findById(transferReference)
                .orElseThrow(() -> new TransferNotFoundException(transferReference));

        return operationRepository.findAllByTransferReference(transferReference);
    }

    @PostMapping("/createTransfer")
    public Transfer createTransfer(@RequestBody Transfer newTransfer) {
        return transferRepository.save(newTransfer);
    }

    @PutMapping("/updateTransfer/{transferReference}")
    public Transfer updateTransfer(@RequestBody Transfer updatedTransfer, @PathVariable Long transferReference) {
        return transferRepository.findById(transferReference)
                .map(existingTransfer -> {

                        if (updatedTransfer.getSenderId() != null) {
                            existingTransfer.setSenderId(updatedTransfer.getSenderId());
                        }
                        if (updatedTransfer.getReceiverId() != null) {
                            existingTransfer.setReceiverId(updatedTransfer.getReceiverId());
                        }
                        if (updatedTransfer.getAmount() != null) {
                            existingTransfer.setAmount(updatedTransfer.getAmount());
                        }
                        if (updatedTransfer.getDate()!= null) {
                            existingTransfer.setDate(updatedTransfer.getDate());
                        }
                        if (updatedTransfer.getType() != null) {
                            existingTransfer.setType(updatedTransfer.getType());
                        }
                        if (updatedTransfer.getState()!= null) {
                            existingTransfer.setState(updatedTransfer.getState());
                        }

                        return transferRepository.save(existingTransfer);
                })
                .orElseThrow(() -> new TransferNotFoundException(transferReference));
    }

    @DeleteMapping("/deleteTransfer/{transferReference}")
    public void deleteTransfer(@PathVariable Long transferReference) {
        transferRepository.deleteById(transferReference);
    }
}