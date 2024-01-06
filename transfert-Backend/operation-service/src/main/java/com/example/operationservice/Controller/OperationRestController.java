package com.example.operationservice.Controller;

import com.example.operationservice.Model.Transfer;
import com.example.operationservice.Repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/OPERATION-SERVICE")
public class OperationRestController {
    @Autowired
    private TransferRepository transferRepository;

}
