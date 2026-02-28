package com.rev.revpasswordmanagerp2.controller;

import com.rev.revpasswordmanagerp2.dto.ChangePasswordRequest;
import com.rev.revpasswordmanagerp2.dto.UpdateProfileRequest;
import com.rev.revpasswordmanagerp2.dto.UpdateSecurityAnswerRequest;
import com.rev.revpasswordmanagerp2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;


    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(
            @RequestBody UpdateProfileRequest request){

        return ResponseEntity.ok(
                userService.updateProfile(request));
    }

    @PutMapping("/change-master-password")
    public ResponseEntity<String> changeMasterPassword(
            @RequestBody ChangePasswordRequest request){

        return ResponseEntity.ok(
                userService.changeMasterPassword(request));
    }

    @PutMapping("/security-questions")
    public ResponseEntity<String> updateSecurityQuestions(
            @RequestBody UpdateSecurityAnswerRequest request){

        return ResponseEntity.ok(
                userService.updateSecurityQuestions(request));
    }
    @GetMapping("/me")
    public ResponseEntity<?> getProfile(@RequestParam String usernameOrEmail) {

        return ResponseEntity.ok(
                userService.getProfile(usernameOrEmail)
        );
    }
}