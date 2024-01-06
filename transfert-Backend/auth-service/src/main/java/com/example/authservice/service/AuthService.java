package com.example.authservice.service;

import com.example.authservice.entity.UserInfo;
import com.example.authservice.repository.UserInfoRepository;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserInfoRepository userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTService jwtService;



    public String saveUser(UserInfo userInfo){
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userRepo.save(userInfo);
        return "user added to the system";
    }


    public String generateToken(String username){
        return jwtService.generateToken(username);
    }

    public void validateToken(String token ){
       jwtService.validateToken(token);
    }

}
