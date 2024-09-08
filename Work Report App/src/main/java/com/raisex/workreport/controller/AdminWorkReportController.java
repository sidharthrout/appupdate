package com.raisex.workreport.controller;

import com.raisex.workreport.service.WorkReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminWorkReportController {
    private final WorkReportService workReportService;

    public AdminWorkReportController(WorkReportService workReportService) {
        this.workReportService = workReportService;
    }

    @GetMapping("/reports")
    public String getAllReports(Model model) {
        model.addAttribute("reports", workReportService.getAllReports());
        return "admin_reports";
    }

    @PostMapping("/reports/confirm/{id}")
    public String confirmReport(@PathVariable Long id) {
        workReportService.updateReportStatus(id, "Confirmed");
        return "redirect:/admin/reports";
    }

    @PostMapping("/reports/reject/{id}")
    public String rejectReport(@PathVariable Long id) {
        workReportService.updateReportStatus(id, "Rejected");
        return "redirect:/admin/reports";
    }
}