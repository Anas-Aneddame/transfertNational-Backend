package com.example.accountservice.controller;

import com.example.accountservice.exception.OperationNotFoundException;
import com.example.accountservice.model.Operation;
import com.example.accountservice.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin("http://localhost:3000")
public class OperationController {

    @Autowired
    private OperationRepository operationRepository;

    @GetMapping("/{transferReference}")
    public List<Operation> getOperationsForTransfer(@PathVariable Long transferReference) {
        return operationRepository.findAllByTransferReference(transferReference);
    }

    @PostMapping("/")
    public Operation createOperation(@RequestBody Operation newOperation) {
        return operationRepository.save(newOperation);
    }


    /*
    @PutMapping("/{operationId}")
    public Operation updateOperation(@RequestBody Operation updatedOperation, @PathVariable Long operationId) {
        return operationRepository.findById(operationId)
                .map(existingOperation -> {

                    existingOperation.setType(updatedOperation.getType());
                    existingOperation.setDate(updatedOperation.getDate());


                    return operationRepository.save(existingOperation);
                })
                .orElseThrow(() -> new OperationNotFoundException(operationId));
    }

     */

    @DeleteMapping("/{operationId}")
    public void deleteOperation(@PathVariable Long operationId) {
        operationRepository.deleteById(operationId);
    }
}