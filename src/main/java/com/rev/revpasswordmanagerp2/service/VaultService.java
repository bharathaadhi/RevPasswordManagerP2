package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.VaultRequest;
import com.rev.revpasswordmanagerp2.entity.PasswordEntry;

import java.util.List;

public interface VaultService<PasswordEntry> {

    String addPassword(VaultRequest request);

    List<PasswordEntry> getAll(String usernameOrEmail);

    String favorite(Long id, boolean value);

    String delete(Long id);

    String update(Long id, VaultRequest request);

    List<com.rev.revpasswordmanagerp2.entity.PasswordEntry> filter(String usernameOrEmail, String category);

    List<com.rev.revpasswordmanagerp2.entity.PasswordEntry> favorites(String user);

    List<com.rev.revpasswordmanagerp2.entity.PasswordEntry> search(String user, String keyword);
}
