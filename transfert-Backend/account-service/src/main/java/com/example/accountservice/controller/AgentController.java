package com.example.accountservice.controller;

import com.example.accountservice.exception.AgentNotFoundException;
import com.example.accountservice.model.Agent;
import com.example.accountservice.model.Transfer;
import com.example.accountservice.repository.AgentRepository;
import com.example.accountservice.repository.TransferRepository;
import com.example.accountservice.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agents")
public class AgentController {

    @Autowired
    private AgentRepository agentRepository;


    @PostMapping
    Agent newAgent(@RequestBody Agent newAgent) {
        return agentRepository.save(newAgent);
    }

    @GetMapping("/allAgents")
    List<Agent> getAllAgents() {
        return agentRepository.findAll();
    }

    @GetMapping("/{AgentId}")
    Agent getAgentById(@PathVariable Long AgentId) {
        return agentRepository.findById(AgentId)
                .orElseThrow(() -> new AgentNotFoundException(AgentId));
    }

    private final AgentService agentService;
    //@Autowired
    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }
    @GetMapping("/email/{email}")
    public Agent getAgentByEmail(@PathVariable String email) {
        Agent agent = agentService.getAgentByEmail(email);
        System.out.println(email);

        return agent;

    }
    @PutMapping("/{agentId}")
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


    @DeleteMapping("/{AgentId}")
    String deleteAgent(@PathVariable Long AgentId){
        if(!agentRepository.existsById(AgentId)){
            throw new AgentNotFoundException(AgentId);
        }
        agentRepository.deleteById(AgentId);
        return  "Agent with id "+AgentId+" has been deleted success.";
    }

}
