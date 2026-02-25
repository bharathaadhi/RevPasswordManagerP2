package com.rev.revpasswordmanagerp2.controller;

import com.rev.revpasswordmanagerp2.dto.*;
import com.rev.revpasswordmanagerp2.service.AuthService;
import jakarta.websocket.EncodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) throws EncodeException {

        String response = authService.registerUser(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request){

        String token = authService.login(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(){

        return ResponseEntity.ok("Logged out successfully");
    }

    @PutMapping("/updateProfile")
    public ResponseEntity<String> updateProfile(@RequestBody UpdateProfileRequest request){

        String response = authService.updateProfile(request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request){

        String response = authService.changePassword(request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request){

        String response = authService.forgotPassword(request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/toggle2fa")
    public ResponseEntity<String> toggle2FA(@RequestBody TwoFactorRequest request){

        String response = authService.toggleTwoFactor(request);
        return ResponseEntity.ok(response);
    }
}