package com.example.accountservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class Transfer {
    @Id
    @GeneratedValue
    private Long transferReference;
    private Long senderId;
    private Long receiverId;
    private String type;
    private Double amount;
    private String state;
    private Date date;

    public Transfer(Long transferReference, Long senderId, Long receiverId, String type, Double amount, String state, Date date) {
        this.transferReference = transferReference;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.type = type;
        this.amount = amount;
        this.state = state;
        this.date = date;
    }

    public Transfer() {
    }

    public Long getTransferReference() {
        return transferReference;
    }

    public void setTransferReference(Long transferReference) {
        this.transferReference = transferReference;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
