package com.gokul.ambrosiabackend.service;


import com.gokul.ambrosiabackend.dto.RegisterRequest;
import com.gokul.ambrosiabackend.exception.ActivationException;
import com.gokul.ambrosiabackend.model.AccountVerificationToken;
import com.gokul.ambrosiabackend.model.NotificationEmail;
import com.gokul.ambrosiabackend.model.User;
import com.gokul.ambrosiabackend.repository.TokenRepository;
import com.gokul.ambrosiabackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static com.gokul.ambrosiabackend.config.Constants.EMAIL_ACTIVATION;

@Service
@AllArgsConstructor
public class AuthService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    TokenRepository tokenRepository;
    MailBuilder mailBuilder;
    MailService mailService;

    @Transactional
    public void register(RegisterRequest registerRequest){
        User user=new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodePassword(registerRequest.getPassword()));
        user.setCreationDate(Instant.now());
        user.setAccountStatus(false);

        userRepository.save(user);

        String token=generateToken(user);
        String message=mailBuilder.build("Welcome to Ambrosia.pl. "+"Please visit the link below to activate your account :"+EMAIL_ACTIVATION+"/"+token);
        mailService.sendMail(new NotificationEmail("Please Activate your account", user.getEmail(),message));
    }

    private String encodePassword(String password){
        return passwordEncoder.encode(password);
    }

    private String generateToken(User user){
        String token= UUID.randomUUID().toString();
        AccountVerificationToken verificationToken=new AccountVerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        tokenRepository.save(verificationToken);
        return token;
    }

    public void verifyToken(String token){
        Optional<AccountVerificationToken>verificationToken=tokenRepository.findByToken(token);
        verificationToken.orElseThrow(()->new ActivationException("Invalid Activation Token"));
        enableAccount(verificationToken.get());
    }

    public void enableAccount(AccountVerificationToken token){
        String username=token.getUser().getUsername();
        User user=userRepository.findByUsername(username).orElseThrow(()->new ActivationException("User not found with username :"+username));
        user.setAccountStatus(true);
        userRepository.save(user);
    }
}
