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

    @PostMapping("/add")
    public String add(@RequestBody VaultRequest request){
        return vaultService.addPassword(request);
    }
    @PutMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @RequestBody VaultRequest request){
        return vaultService.update(id,request);
    }
    @GetMapping("/all")
    public List<PasswordEntry> getAll(@RequestParam String usernameOrEmail){
        return vaultService.getAll(usernameOrEmail);
    }
    @GetMapping("/search")
    public List<PasswordEntry> search(@RequestParam String user,
                                      @RequestParam String keyword){
        return vaultService.search(user,keyword);
    }
    @GetMapping("/filter/{category}")
    public List<PasswordEntry> filter(@RequestParam String user,
                                      @PathVariable String category){
        return vaultService.filter(user,category);
    }
    @PatchMapping("/{id}/favorite")
    public String favorite(@PathVariable Long id,
                           @RequestParam boolean value){
        return vaultService.favorite(id,value);
    }

    @GetMapping("/favorites")
    public List<PasswordEntry> favorites(@RequestParam String user){
        return vaultService.favorites(user);
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        return vaultService.delete(id);
    }
}