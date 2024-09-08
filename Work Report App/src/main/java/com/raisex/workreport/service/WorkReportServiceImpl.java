package com.raisex.workreport.service;

import com.raisex.workreport.model.WorkReport;
import com.raisex.workreport.repository.WorkReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class WorkReportServiceImpl implements WorkReportService {

    private final WorkReportRepository workReportRepository;

    public WorkReportServiceImpl(WorkReportRepository workReportRepository) {
        this.workReportRepository = workReportRepository;
    }

    @Override
    @Transactional
    public void saveReport(WorkReport workReport) {
        calculateTotalTime(workReport); // Calculate total time before saving
        workReportRepository.save(workReport);
    }

    @Override
    @Transactional
    public void updateReport(Long id, WorkReport updatedReport) {
        workReportRepository.findById(id)
                .ifPresent(report -> {
                    report.setReportDate(updatedReport.getReportDate());
                    report.setStartTime(updatedReport.getStartTime());
                    report.setEndTime(updatedReport.getEndTime());
                    report.setBreakTime(updatedReport.getBreakTime());
                    report.setTotalTime(updatedReport.getTotalTime());
                    report.setWorkDescription(updatedReport.getWorkDescription());
                    report.setStatus(updatedReport.getStatus());
                    calculateTotalTime(report);
                    workReportRepository.save(report);
                });
    }

    @Override
    public List<WorkReport> getAllReports() {
        return workReportRepository.findAll();
    }

    @Override
    public Optional<WorkReport> findById(Long id) {
        return workReportRepository.findById(id);
    }

    @Override
    public void deleteReport(Long id) {
        workReportRepository.deleteById(id);
    }

    @Override
    public List<WorkReport> getReportsForMonth(int year, int month) {
        try {
            return workReportRepository.findByYearAndMonth(year, month);
        } catch (Exception e) {
            System.err.println("Error fetching reports for year: " + year + ", month: " + month);
            throw new RuntimeException("Database query failed", e);
        }
    }

    @Override
    public void updateReportStatus(Long id, String status) {
        Optional<WorkReport> optionalReport = workReportRepository.findById(id);
        if (optionalReport.isPresent()) {
            WorkReport report = optionalReport.get();
            report.setStatus(status);
            workReportRepository.save(report);
        }
    }

    private void calculateTotalTime(WorkReport workReport) {
        LocalTime startTime = workReport.getStartTime();
        LocalTime endTime = workReport.getEndTime();
        LocalTime breakTime = workReport.getBreakTime();
        int breakMinutes = breakTime != null ? breakTime.getHour() * 60 + breakTime.getMinute() : 0;

        if (startTime != null && endTime != null) {
            Duration workDuration = Duration.between(startTime, endTime).minusMinutes(breakMinutes);
            if (workDuration.isNegative()) {
                workDuration = workDuration.plus(Duration.ofHours(24));
            }
            workReport.setTotalTime(workDuration.toHours() + "h " + workDuration.toMinutesPart() + "m");
        }
    }
}