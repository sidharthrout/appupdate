package com.raisex.workreport.service;

import com.raisex.workreport.model.WorkReport;

import java.util.List;
import java.util.Optional;

public interface WorkReportService {
    List<WorkReport> getAllReports();
    void saveReport(WorkReport workReport);
    void updateReport(Long id, WorkReport workReport);
    void deleteReport(Long id);
    Optional<WorkReport> findById(Long id);
    List<WorkReport> getReportsForMonth(int year, int month);
    void updateReportStatus(Long id, String status);
}