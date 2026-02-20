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

    @PostMapping("/add")
    public String add(@RequestBody VaultRequest request){
        return vaultService.addPassword(request);
    }

    @PutMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @RequestBody VaultRequest request){
        return vaultService.update(id, request);
    }

    @GetMapping("/all")
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

    @GetMapping("/sort")
    public List<PasswordEntryDTO> sort(
            @RequestParam String usernameOrEmail,
            @RequestParam String sortBy){
        return vaultService.sort(usernameOrEmail, sortBy);
    }

    @PostMapping("/view")
    public PasswordEntryDTO view(
            @RequestBody ViewPasswordRequest request){
        return vaultService.viewWithVerification(request);
    }

    @GetMapping("/export")
    public List<PasswordEntryDTO> export(
            @RequestParam String usernameOrEmail){
        return vaultService.getAll(usernameOrEmail);
    }

    @PostMapping("/import")
    public String importVault(
            @RequestParam String usernameOrEmail,
            @RequestBody List<VaultRequest> requests){

        for(VaultRequest request : requests){
            request.setUsernameOrEmail(usernameOrEmail);
            vaultService.addPassword(request);
        }

        return "Import successful";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        return vaultService.delete(id);
    }
}