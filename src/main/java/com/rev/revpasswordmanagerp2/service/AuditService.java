package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.PasswordEntryAuditDTO;
import com.rev.revpasswordmanagerp2.model.User;

import java.util.List;
import java.util.Map;

public interface AuditService {

    List<PasswordEntryAuditDTO> getWeakPasswords(User user);

    List<PasswordEntryAuditDTO> getReusedPasswords(User user);

    Map<String, Object> generateSecurityReport(User user);

    String securityAlert(User user);
}