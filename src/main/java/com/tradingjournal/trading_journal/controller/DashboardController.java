package com.tradingjournal.trading_journal.controller;

import com.tradingjournal.trading_journal.dtos.DashboardDTO;
import com.tradingjournal.trading_journal.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public DashboardDTO getDashboard() {
        String userEmail = getCurrentUserEmail();
        return dashboardService.getDashboardData(userEmail);
    }

    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}