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

    // ================= ADD PASSWORD =================

    @PostMapping
    public String add(@RequestBody VaultRequest request){
        return vaultService.addPassword(request);
    }

    // ================= UPDATE PASSWORD =================

    @PutMapping("/{id}")
    public String update(@PathVariable Long id,
                         @RequestBody VaultRequest request){
        return vaultService.update(id, request);
    }

    // ================= GET ALL =================

    @GetMapping
    public List<PasswordEntryDTO> getAll(
            @RequestParam String usernameOrEmail){
        return vaultService.getAll(usernameOrEmail);
    }

    // ================= SEARCH =================

    @GetMapping("/search")
    public List<PasswordEntryDTO> search(
            @RequestParam String usernameOrEmail,
            @RequestParam String keyword){
        return vaultService.search(usernameOrEmail, keyword);
    }

    // ================= FILTER =================

    @GetMapping("/filter/{category}")
    public List<PasswordEntryDTO> filter(
            @RequestParam String usernameOrEmail,
            @PathVariable String category){
        return vaultService.filter(usernameOrEmail, category);
    }

    // ================= FAVORITE TOGGLE =================

    @PatchMapping("/{id}/favorite")
    public String favorite(@PathVariable Long id,
                           @RequestParam boolean value){
        return vaultService.favorite(id, value);
    }

    // ================= GET FAVORITES =================

    @GetMapping("/favorites")
    public List<PasswordEntryDTO> getFavorites(
            @RequestParam String usernameOrEmail){
        return vaultService.getFavorites(usernameOrEmail);
    }

    // ================= SORT =================

    @GetMapping("/sort")
    public List<PasswordEntryDTO> sort(
            @RequestParam String usernameOrEmail,
            @RequestParam String sortBy){
        return vaultService.sort(usernameOrEmail, sortBy);
    }

    // ================= VIEW WITH MASTER PASSWORD =================

    @PostMapping("/view")
    public ResponseEntity<?> viewPassword(
            @RequestBody ViewPasswordRequest request) {

        return ResponseEntity.ok(
                vaultService.viewWithVerification(request)
        );
    }

    // ================= EXPORT VAULT =================

    @PostMapping("/export")
    public List<VaultExportDTO> export(
            @RequestBody ExportVaultRequest request) {

        return vaultService.exportVault(request);
    }

    // ================= IMPORT VAULT =================

    @PostMapping("/import")
    public String importVault(
            @RequestBody ImportVaultRequest request){

        vaultService.importVault(request);
        return "Import Successful";
    }

    // ================= DELETE =================

    @PostMapping("/delete")
    public String delete(
            @RequestBody DeletePasswordRequest request){

        return vaultService.delete(request);
    }

    // ================= OLD PASSWORD =================

    @GetMapping("/old")
    public List<PasswordEntryDTO> getOld(
            @RequestParam String usernameOrEmail){
        return vaultService.getOldPasswords(usernameOrEmail);
    }
}