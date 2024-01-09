package com.example.operationservice.Service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    private JavaMailSender mailSender;
    public EmailSenderService(JavaMailSender mailSender){
        this.mailSender=mailSender;
    }
    public void sendSimpleEmail(String toEmail,String subject,String body){
        System.out.println("Mail started...");
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom("projetai_2024@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);
        System.out.println("Mail Send...");
    }
}
