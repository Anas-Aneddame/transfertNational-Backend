package com.example.accountservice.controller;

import com.example.accountservice.exception.AgentNotFoundException;
import com.example.accountservice.model.Agent;
import com.example.accountservice.model.Transfer;
import com.example.accountservice.repository.AgentRepository;
import com.example.accountservice.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class AgentController {

    @Autowired
    private AgentRepository agentRepository;

    /*@Autowired
    private TransferRepository transferRepository;

    @GetMapping("/agents/{agentId}/transfers")
    public List<Transfer> getAllTransfersForAgent(@PathVariable Long agentId) {
        return transferRepository.findAllBySenderIdOrReceiverId(agentId, agentId);
    }*/

    @PostMapping("/agent")
    Agent newAgent(@RequestBody Agent newAgent) {
        return agentRepository.save(newAgent);
    }

    @GetMapping("/agents")
    List<Agent> getAllAgents() {
        return agentRepository.findAll();
    }

    @GetMapping("/agent/{AgentId}")
    Agent getAgentById(@PathVariable Long AgentId) {
        return agentRepository.findById(AgentId)
                .orElseThrow(() -> new AgentNotFoundException(AgentId));
    }

    @PutMapping("/agent/{agentId}")
    Agent updateAgent(@RequestBody Agent updatedAgent, @PathVariable Long agentId) {
        return agentRepository.findById(agentId)
                .map(existingAgent -> {
                    if (updatedAgent.getFirstName() != null) {
                        existingAgent.setFirstName(updatedAgent.getFirstName());
                    }
                    if (updatedAgent.getLastName() != null) {
                        existingAgent.setLastName(updatedAgent.getLastName());
                    }
                    if (updatedAgent.getCNE() != null) {
                        existingAgent.setCNE(updatedAgent.getCNE());
                    }
                    if (updatedAgent.getEmail() != null) {
                        existingAgent.setEmail(updatedAgent.getEmail());
                    }
                    if (updatedAgent.getPhone() != null) {
                        existingAgent.setPhone(updatedAgent.getPhone());
                    }
                    if (updatedAgent.getRole() != null) {
                        existingAgent.setRole(updatedAgent.getRole());
                    }

                    return agentRepository.save(existingAgent);
                })
                .orElseThrow(() -> new AgentNotFoundException(agentId));
    }


    @DeleteMapping("/agent/{AgentId}")
    String deleteAgent(@PathVariable Long AgentId){
        if(!agentRepository.existsById(AgentId)){
            throw new AgentNotFoundException(AgentId);
        }
        agentRepository.deleteById(AgentId);
        return  "Agent with id "+AgentId+" has been deleted success.";
    }

}
