package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.PasswordEntryDTO;
import com.rev.revpasswordmanagerp2.dto.VaultRequest;

import java.util.List;

public interface VaultService<PasswordEntry> {

    String addPassword(VaultRequest request);

    List<PasswordEntryDTO> getAll(String usernameOrEmail);

    String favorite(Long id, boolean value);

    String delete(Long id);

    String update(Long id, VaultRequest request);

    List<PasswordEntryDTO> favorites(String usernameOrEmail);

    List<PasswordEntryDTO> filter(String usernameOrEmail, String category);

    List<PasswordEntryDTO> search(String usernameOrEmail,String keyword);
}
