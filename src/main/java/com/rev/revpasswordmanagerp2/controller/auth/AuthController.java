package com.rev.revpasswordmanagerp2.controller.auth;

import com.rev.revpasswordmanagerp2.dto.ChangePasswordRequest;
import com.rev.revpasswordmanagerp2.dto.ForgotPasswordRequest;
import com.rev.revpasswordmanagerp2.dto.LoginRequest;
import com.rev.revpasswordmanagerp2.dto.RegisterRequest;
import com.rev.revpasswordmanagerp2.service.auth.AuthService;
import jakarta.websocket.EncodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    // ===========================================
// ✅ CHANGE PASSWORD API
// ===========================================
    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordRequest request) {

        String response = authService.changePassword(request);
        return ResponseEntity.ok(response);
    }
    // ===========================================
// ✅ FORGOT PASSWORD
// ===========================================
    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(
            @RequestBody ForgotPasswordRequest request) {

        String response = authService.forgotPassword(request);
        return ResponseEntity.ok(response);
    }





    @Autowired
    private AuthService authService;


    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) throws EncodeException {
        return authService.registerUser(request);
    }


    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request){
        return authService.login(request);
    }
}
