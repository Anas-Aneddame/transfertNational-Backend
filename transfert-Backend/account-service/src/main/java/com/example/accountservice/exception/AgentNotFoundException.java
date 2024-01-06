package com.example.accountservice.exception;

public class AgentNotFoundException extends RuntimeException{
    public AgentNotFoundException(Long AgentId){
        super("Could not found the Agent with id "+ AgentId);
    }
}
