package com.rev.revpasswordmanagerp2.controller;

import com.rev.revpasswordmanagerp2.dto.*;
import com.rev.revpasswordmanagerp2.service.VaultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vault")
@RequiredArgsConstructor
public class VaultController {

    private final VaultService vaultService;

    @PostMapping
    public String add(@RequestBody VaultRequest request){
        return vaultService.addPassword(request);
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id,
                         @RequestBody VaultRequest request){
        return vaultService.update(id, request);
    }

    @GetMapping
    public List<PasswordEntryDTO> getAll(
            @RequestParam String usernameOrEmail){
        return vaultService.getAll(usernameOrEmail);
    }

    @GetMapping("/search")
    public List<PasswordEntryDTO> search(
            @RequestParam String usernameOrEmail,
            @RequestParam String keyword){
        return vaultService.search(usernameOrEmail, keyword);
    }

    @GetMapping("/filter/{category}")
    public List<PasswordEntryDTO> filter(
            @RequestParam String usernameOrEmail,
            @PathVariable String category){
        return vaultService.filter(usernameOrEmail, category);
    }

    @PatchMapping("/{id}/favorite")
    public String favorite(@PathVariable Long id,
                           @RequestParam boolean value){
        return vaultService.favorite(id, value);
    }

    @GetMapping("/favorites")
    public List<PasswordEntryDTO> getFavorites(
            @RequestParam String usernameOrEmail){
        return vaultService.getFavorites(usernameOrEmail);
    }

    @GetMapping("/sort")
    public List<PasswordEntryDTO> sort(
            @RequestParam String usernameOrEmail,
            @RequestParam String sortBy){
        return vaultService.sort(usernameOrEmail, sortBy);
    }

    @PostMapping("/view")
    public ResponseEntity<?> viewPassword(
            @RequestBody ViewPasswordRequest request) {

        return ResponseEntity.ok(
                vaultService.viewWithVerification(request)
        );
    }

    @PostMapping("/export")
    public List<PasswordEntryDTO> export(
            @RequestBody ExportVaultRequest request) {

        return vaultService.exportVault(request);
    }

    @PostMapping("/import")
    public String importVault(
            @RequestBody ImportVaultRequest request){

        vaultService.importVault(request);
        return "Import Successful";
    }

    @PostMapping("/delete")
    public String delete(
            @RequestBody DeletePasswordRequest request){

        return vaultService.delete(request);
    }

    @GetMapping("/old")
    public List<PasswordEntryDTO> getOld(
            @RequestParam String usernameOrEmail){
        return vaultService.getOldPasswords(usernameOrEmail);
    }
}