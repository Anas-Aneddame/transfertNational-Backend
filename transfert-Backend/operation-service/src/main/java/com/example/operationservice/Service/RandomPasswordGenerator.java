package com.example.operationservice.Service;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.stereotype.Service;

import static org.springframework.beans.MethodInvocationException.ERROR_CODE;

@Service
public class RandomPasswordGenerator {
    public  String generatePassayPassword() {
        PasswordGenerator gen = new PasswordGenerator();
        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(2);

        String password = gen.generatePassword(6,digitRule);
        return password;
    }

}