package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.PasswordEntryDTO;
import com.rev.revpasswordmanagerp2.dto.VaultRequest;
import com.rev.revpasswordmanagerp2.dto.ViewPasswordRequest;

import java.util.List;

public interface VaultService {

    // ================= BASIC CRUD =================

    String addPassword(VaultRequest request);

    List<PasswordEntryDTO> getAll(String usernameOrEmail);

    String update(Long id, VaultRequest request);

    String delete(Long id);

    // ================= FAVORITES =================

    String favorite(Long id, boolean value);

    List<PasswordEntryDTO> favorites(String usernameOrEmail);

    // ================= SEARCH / FILTER / SORT =================

    List<PasswordEntryDTO> filter(String usernameOrEmail, String category);

    List<PasswordEntryDTO> search(String usernameOrEmail,String keyword);

    List<PasswordEntryDTO> sort(String usernameOrEmail, String sortBy);

    // ================= VIEW WITH MASTER PASSWORD =================

    PasswordEntryDTO viewWithVerification(ViewPasswordRequest request);

    // ================= IMPORT / EXPORT =================

    List<PasswordEntryDTO> exportVault(String usernameOrEmail);

    String importVault(String usernameOrEmail, List<VaultRequest> requests);

}