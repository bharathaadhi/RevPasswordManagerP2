package com.rev.revpasswordmanagerp2.controller;

import com.rev.revpasswordmanagerp2.dto.DashboardResponse;
import com.rev.revpasswordmanagerp2.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public DashboardResponse getDashboard(
            @RequestParam String usernameOrEmail) {

        return dashboardService.getDashboard(usernameOrEmail);
    }
}