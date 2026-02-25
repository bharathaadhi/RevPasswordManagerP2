package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.PasswordEntryDTO;
import com.rev.revpasswordmanagerp2.dto.VaultRequest;
import com.rev.revpasswordmanagerp2.dto.ViewPasswordRequest;
import com.rev.revpasswordmanagerp2.dto.ViewPasswordResponseDTO;

import java.util.List;

public interface VaultService {



    String addPassword(VaultRequest request);

    List<PasswordEntryDTO> getAll(String usernameOrEmail);

    String update(Long id, VaultRequest request);

    String delete(Long id);


    String favorite(Long id, boolean value);

    List<PasswordEntryDTO> getFavorites(String usernameOrEmail);


    List<PasswordEntryDTO> filter(String usernameOrEmail, String category);

    List<PasswordEntryDTO> search(String usernameOrEmail, String keyword);

    List<PasswordEntryDTO> sort(String usernameOrEmail, String sortBy);


    ViewPasswordResponseDTO viewWithVerification(ViewPasswordRequest request);



    List<PasswordEntryDTO> getOldPasswords(String usernameOrEmail);



    List<PasswordEntryDTO> exportVault(String usernameOrEmail);

    String importVault(String usernameOrEmail, List<VaultRequest> requests);
}