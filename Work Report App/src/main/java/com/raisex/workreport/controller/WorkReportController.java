package com.raisex.workreport.controller;

import com.raisex.workreport.model.WorkReport;
import com.raisex.workreport.service.WorkReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Controller
@RequestMapping("/reports")
public class WorkReportController {
    private final WorkReportService workReportService;

    public WorkReportController(WorkReportService workReportService) {
        this.workReportService = workReportService;
    }

    @GetMapping
    public String getAllReports(Model model) {
        model.addAttribute("reports", workReportService.getAllReports());
        model.addAttribute("workReport", new WorkReport());
        return "work_reports";
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createOrUpdateReport(@ModelAttribute WorkReport workReport) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (workReport.getId() != null) {
                workReportService.updateReport(workReport.getId(), workReport);
            } else {
                workReportService.saveReport(workReport);
            }
            response.put("status", "success");
            response.put("message", "Report saved successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to save report: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        WorkReport workReport = workReportService.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Invalid report Id: " + id));
        model.addAttribute("workReport", workReport);
        return "work_reports";
    }

    @PostMapping("/update/{id}")
    public String updateReport(@PathVariable Long id, @ModelAttribute WorkReport workReport) {
        workReportService.updateReport(id, workReport);
        return "redirect:/reports";
    }

    @GetMapping("/delete/{id}")
    public String deleteReport(@PathVariable Long id) {
        workReportService.deleteReport(id);
        return "redirect:/reports";
    }

    @GetMapping("/monthly")
    @ResponseBody
    public List<WorkReport> getMonthlyReports(@RequestParam("year") int year, @RequestParam("month") int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();

        List<WorkReport> existingReports = workReportService.getReportsForMonth(year, month);
        List<WorkReport> fullMonthReports = new ArrayList<>();

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = LocalDate.of(year, month, day);
            Optional<WorkReport> reportForDay = existingReports.stream()
                    .filter(report -> report.getReportDate().equals(date))
                    .findFirst();

            // If a report exists for this day, use it. Otherwise, create a placeholder.
            WorkReport report = reportForDay.orElseGet(() -> new WorkReport(date, null, null, null, "", "Incomplete"));
            fullMonthReports.add(report);
        }

        return fullMonthReports;
    }

    @PostMapping("/confirm/{id}")
    public String confirmReport(@PathVariable Long id) {
        workReportService.updateReportStatus(id, "Confirmed");
        return "redirect:/reports";
    }

    @PostMapping("/requestApproval/{id}")
    public String requestApproval(@PathVariable Long id) {
        workReportService.updateReportStatus(id, "Approval Requested");
        return "redirect:/reports";
    }
}