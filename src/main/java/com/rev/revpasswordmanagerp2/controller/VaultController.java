package com.rev.revpasswordmanagerp2.controller;

import com.rev.revpasswordmanagerp2.dto.PasswordEntryDTO;
import com.rev.revpasswordmanagerp2.dto.VaultRequest;
import com.rev.revpasswordmanagerp2.dto.ViewPasswordRequest;
import com.rev.revpasswordmanagerp2.service.VaultService;
import lombok.RequiredArgsConstructor;
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
        return vaultService.favorite(id,value);
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
    public PasswordEntryDTO view(
            @RequestBody ViewPasswordRequest request){
        return vaultService.viewWithVerification(request);
    }

    // ================= EXPORT VAULT =================

    @GetMapping("/export")
    public List<PasswordEntryDTO> export(
            @RequestParam String usernameOrEmail){
        return vaultService.exportVault(usernameOrEmail);
    }

    // ================= IMPORT VAULT =================

    @PostMapping("/import")
    public String importVault(
            @RequestParam String usernameOrEmail,
            @RequestBody List<VaultRequest> requests){

        return vaultService.importVault(usernameOrEmail, requests);
    }

    // ================= DELETE =================

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id){
        return vaultService.delete(id);
    }
}