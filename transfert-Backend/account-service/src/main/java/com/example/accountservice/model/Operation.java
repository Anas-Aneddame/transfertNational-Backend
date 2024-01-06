package com.example.accountservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class Operation {
    @Id
    @GeneratedValue
    private Long OperationId;
    private Long transferReference;
    private Long executerId;
    private String type;
    private Date date;

    public Long getOperationId() {
        return OperationId;
    }

    public void setOperationId(Long operationId) {
        OperationId = operationId;
    }

    public Long getTransferReference() {
        return transferReference;
    }

    public void setTransferReference(Long transferReference) {
        this.transferReference = transferReference;
    }

    public Long getExecuterId() {
        return executerId;
    }

    public void setExecuterId(Long executerId) {
        this.executerId = executerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Operation(Long operationId, Long transferReference, Long executerId, String type, Date date) {
        OperationId = operationId;
        this.transferReference = transferReference;
        this.executerId = executerId;
        this.type = type;
        this.date = date;
    }

    public Operation() {
    }
}
