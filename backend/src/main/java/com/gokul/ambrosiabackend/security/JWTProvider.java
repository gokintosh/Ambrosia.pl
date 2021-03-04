package com.gokul.ambrosiabackend.security;


import com.gokul.ambrosiabackend.exception.ActivationException;
import io.jsonwebtoken.Jwts;
import org.hibernate.mapping.PrimaryKey;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

@Service
public class JWTProvider {
    private KeyStore keyStore;


    @PostConstruct
    public void init(){
        try{
            keyStore=KeyStore.getInstance("JKS");
            InputStream resourceStream=getClass().getResourceAsStream("/ambrosia.jks");
            keyStore.load(resourceStream,"malayalam".toCharArray());
        }catch(KeyStoreException| CertificateException| NoSuchAlgorithmException| IOException e){
            throw new ActivationException("Exception occurred while loading keystore");
        }
    }

    public String generateToken(Authentication authentication){
        User princ=(User)authentication.getPrincipal();
        return Jwts.builder().setSubject(princ.getUsername()).signWith(getPrivKey()).compact();
    }

    private PrivateKey getPrivKey(){
        try{
            return (PrivateKey)keyStore.getKey("ambrosia","malayalam".toCharArray());
        }catch (KeyStoreException|NoSuchAlgorithmException| UnrecoverableKeyException e){
            throw new ActivationException("Exception occurred while retrieving public key");
        }
    }
}
