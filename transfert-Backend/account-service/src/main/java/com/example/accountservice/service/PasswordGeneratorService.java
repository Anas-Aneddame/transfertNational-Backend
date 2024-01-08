package com.example.accountservice.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PasswordGeneratorService {

    public String generatePassword() {
        Random random = new Random();
        int randomDigits = 1000 + random.nextInt(9000);

        String password = "transfert" + randomDigits;

        return password;
    }
}
