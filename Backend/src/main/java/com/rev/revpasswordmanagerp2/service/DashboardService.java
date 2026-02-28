package com.rev.revpasswordmanagerp2.service;

import com.rev.revpasswordmanagerp2.dto.DashboardResponse;

public interface DashboardService {
    DashboardResponse getDashboard(String usernameOrEmail);
}