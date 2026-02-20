package com.rev.revpasswordmanagerp2.controller;

import com.rev.revpasswordmanagerp2.dto.VaultRequest;
import com.rev.revpasswordmanagerp2.entity.PasswordEntry;
import com.rev.revpasswordmanagerp2.service.VaultService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vault")
@RequiredArgsConstructor
public class VaultController {

    private final VaultService vaultService;

    // ================================
    // ADD PASSWORD ENTRY
    // ================================
    @PostMapping("/add")
    public String add(@RequestBody VaultRequest request){
        return vaultService.addPassword(request);
    }

    // ================================
    // UPDATE PASSWORD ENTRY
    // ================================
    @PutMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @RequestBody VaultRequest request){
        return vaultService.update(id,request);
    }

    // ================================
    // GET ALL PASSWORDS
    // ================================
    @GetMapping("/all")
    public List<PasswordEntry> getAll(@RequestParam String usernameOrEmail){
        return vaultService.getAll(usernameOrEmail);
    }

    // ================================
    // SEARCH PASSWORDS
    // ================================
    @GetMapping("/search")
    public List<PasswordEntry> search(@RequestParam String user,
                                      @RequestParam String keyword){
        return vaultService.search(user,keyword);
    }

    // ================================
    // FILTER BY CATEGORY
    // ================================
    @GetMapping("/filter/{category}")
    public List<PasswordEntry> filter(@RequestParam String user,
                                      @PathVariable String category){
        return vaultService.filter(user,category);
    }

    // ================================
    // FAVORITE TOGGLE
    // ================================
    @PatchMapping("/{id}/favorite")
    public String favorite(@PathVariable Long id,
                           @RequestParam boolean value){
        return vaultService.favorite(id,value);
    }

    // ================================
    // GET FAVORITE LIST
    // ================================
    @GetMapping("/favorites")
    public List<PasswordEntry> favorites(@RequestParam String user){
        return vaultService.favorites(user);
    }

    // ================================
    // DELETE PASSWORD
    // ================================
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        return vaultService.delete(id);
    }
}