package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.*;

import java.util.List;

public interface VaultService {

    // ================= BASIC CRUD =================

    String addPassword(VaultRequest request);

    List<PasswordEntryDTO> getAll(String usernameOrEmail);

    String update(Long id, VaultRequest request);

    String delete(DeletePasswordRequest request);

    // ================= FAVORITES =================

    String favorite(Long id, boolean value);

    List<PasswordEntryDTO> getFavorites(String usernameOrEmail);

    // ================= SEARCH / FILTER / SORT =================

    List<PasswordEntryDTO> filter(String usernameOrEmail, String category);

    List<PasswordEntryDTO> search(String usernameOrEmail, String keyword);

    List<PasswordEntryDTO> sort(String usernameOrEmail, String sortBy);

    // ================= VIEW WITH MASTER PASSWORD =================

    ViewPasswordResponseDTO viewWithVerification(ViewPasswordRequest request);

    // ================= OLD PASSWORDS =================

    List<PasswordEntryDTO> getOldPasswords(String usernameOrEmail);

    // ================= IMPORT / EXPORT =================

    // ========= IMPORT / EXPORT =========

    List<VaultExportDTO> exportVault(
            ExportVaultRequest request
    );

    void importVault(
            ImportVaultRequest request
    );
}