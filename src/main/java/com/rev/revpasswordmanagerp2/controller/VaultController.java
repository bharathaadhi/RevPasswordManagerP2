package com.rev.revpasswordmanagerp2.controller;

import com.rev.revpasswordmanagerp2.dto.PasswordEntryDTO;
import com.rev.revpasswordmanagerp2.dto.VaultRequest;
import com.rev.revpasswordmanagerp2.dto.ViewPasswordRequest;
import com.rev.revpasswordmanagerp2.dto.ViewPasswordResponseDTO;
import com.rev.revpasswordmanagerp2.service.VaultService;
import lombok.RequiredArgsConstructor;
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
    public ViewPasswordResponseDTO view(
            @RequestBody ViewPasswordRequest request){
        return vaultService.viewWithVerification(request);
    }



    @GetMapping("/export")
    public List<PasswordEntryDTO> export(
            @RequestParam String usernameOrEmail){
        return vaultService.exportVault(usernameOrEmail);
    }


    @PostMapping("/import")
    public String importVault(
            @RequestParam String usernameOrEmail,
            @RequestBody List<VaultRequest> requests){

        return vaultService.importVault(usernameOrEmail, requests);
    }



    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id){
        return vaultService.delete(id);
    }



    @GetMapping("/old")
    public List<PasswordEntryDTO> getOld(
            @RequestParam String usernameOrEmail){
        return vaultService.getOldPasswords(usernameOrEmail);
    }
}