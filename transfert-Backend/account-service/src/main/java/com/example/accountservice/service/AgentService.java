package com.example.accountservice.service;

import com.example.accountservice.model.Agent;
import com.example.accountservice.repository.AgentRepository;
import org.springframework.stereotype.Service;

@Service
public class AgentService {
    private final AgentRepository agentRepository;

    public AgentService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    public Agent getAgentByEmail(String email) {
        return agentRepository.findByEmail(email);
    }
}