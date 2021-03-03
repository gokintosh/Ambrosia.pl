package com.gokul.ambrosiabackend.controller;


import com.gokul.ambrosiabackend.dto.RegisterRequest;
import com.gokul.ambrosiabackend.service.AuthService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {


    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequest registerRequest){
        authService.register(registerRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/verify/{token}")
    public ResponseEntity verify(@PathVariable String token){
        authService.verifyToken(token);
        return new ResponseEntity<>("Account Activated ",HttpStatus.OK);
    }
}
