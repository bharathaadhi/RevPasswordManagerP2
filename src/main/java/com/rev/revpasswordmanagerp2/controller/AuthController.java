package com.rev.revpasswordmanagerp2.controller;

import com.rev.revpasswordmanagerp2.dto.*;
import com.rev.revpasswordmanagerp2.service.AuthService;
import jakarta.websocket.EncodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // ================= LOGIN =================

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request){

        try {

            String token = authService.login(request);

            if(token == null || token.equals("INVALID_CREDENTIALS")){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid username or password");
            }

            return ResponseEntity.ok(token);

        } catch (Exception e){

            //  prevent 500 crash
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }

    // ================= REGISTER =================

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) throws EncodeException {
        return ResponseEntity.ok(authService.registerUser(request));
    }

    // ================= DASHBOARD =================

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> dashboard(
            @RequestParam String usernameOrEmail){

        return ResponseEntity.ok(authService.getDashboardSummary(usernameOrEmail));
    }

    // ================= CHANGE PASSWORD =================

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordRequest request) {

        return ResponseEntity.ok(authService.changePassword(request));
    }

    // ================= FORGOT PASSWORD =================

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(
            @RequestBody ForgotPasswordRequest request) {

        return ResponseEntity.ok(authService.forgotPassword(request));
    }

    // ================= LOGOUT =================

    @PostMapping("/logout")
    public ResponseEntity<String> logout(){
        return ResponseEntity.ok("Logged out successfully");
    }

    // ================= TOGGLE 2FA =================

    @PostMapping("/toggle2fa")
    public ResponseEntity<String> toggle2FA(@RequestBody TwoFactorRequest request) {
        return ResponseEntity.ok(authService.toggleTwoFactor(request));
    }

    // ================= UPDATE PROFILE =================

    @PutMapping("/updateProfile")
    public ResponseEntity<String> updateProfile(@RequestBody UpdateProfileRequest request){
        return ResponseEntity.ok(authService.updateProfile(request));
    }
}