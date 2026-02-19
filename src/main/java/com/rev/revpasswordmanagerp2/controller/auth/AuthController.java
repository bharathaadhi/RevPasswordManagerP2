package com.rev.revpasswordmanagerp2.controller.auth;

import com.rev.revpasswordmanagerp2.dto.*;
import com.rev.revpasswordmanagerp2.service.auth.AuthService;
import jakarta.websocket.EncodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.rev.revpasswordmanagerp2.dto.TwoFactorRequest;

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

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(
            @RequestBody ForgotPasswordRequest request) {

        String response = authService.forgotPassword(request);
        return ResponseEntity.ok(response);
    }





    @Autowired
    private AuthService authService;

    @PostMapping("/logout")
    public String logout(){
        return "Logged out successfully";
    }


    @PostMapping("/toggle2fa")
    public ResponseEntity<String> toggle2FA(@RequestBody TwoFactorRequest request) {

        String response = authService.toggleTwoFactor(request);

        return ResponseEntity.ok(response);
    }





    // ===========================================
// ✅ DASHBOARD SUMMARY API
// ===========================================
    @GetMapping("/dashboard")
    public DashboardResponse dashboard(
            @RequestParam String usernameOrEmail){

        return authService.getDashboardSummary(usernameOrEmail);
    }




    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) throws EncodeException {
        return authService.registerUser(request);
    }


    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request){
        return authService.login(request);
    }
}
