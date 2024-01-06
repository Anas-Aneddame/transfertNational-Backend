package com.example.accountservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Agent {

    @Id
    @GeneratedValue
    private Long AgentId;
    private String FirstName;
    private String LastName;
    private String CNE;
    private String Email;
    private String Phone;
    private String Role;


    public Agent(Long agentId, String firstName, String lastName, String CNE, String email, String phone, String role) {
        AgentId = agentId;
        FirstName = firstName;
        LastName = lastName;
        this.CNE = CNE;
        Email = email;
        Phone = phone;
        Role = role;
    }


    public Agent() {
    }

    public Long getAgentId() {
        return AgentId;
    }

    public void setAgentId(Long agentId) {
        AgentId = agentId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getCNE() {
        return CNE;
    }

    public void setCNE(String CNE) {
        this.CNE = CNE;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }
}
