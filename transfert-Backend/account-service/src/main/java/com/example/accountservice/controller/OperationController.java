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

    @GetMapping("/operation/{transferReference}")
    public List<Operation> getOperationsForTransfer(@PathVariable Long transferReference) {
        return operationRepository.findAllByTransferReference(transferReference);
    }

}

