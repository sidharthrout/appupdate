package com.raisex.workreport.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class WorkReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate reportDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime breakTime;
    private String totalTime; // Store total time as a string (calculated value)
    private String workDescription;
    private String status;

    // Constructors
    public WorkReport() {}

    public WorkReport(LocalDate reportDate, LocalTime startTime, LocalTime endTime, LocalTime breakTime, String workDescription, String status) {
        this.reportDate = reportDate;
        this.startTime = startTime != null ? startTime : LocalTime.MIN;
        this.endTime = endTime != null ? endTime : LocalTime.MIN;
        this.breakTime = breakTime != null ? breakTime : LocalTime.MIN;
        this.totalTime = calculateTotalTime(); // Calculate total time when initialized
        this.workDescription = workDescription;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
        this.totalTime = calculateTotalTime(); // Recalculate total time when start time changes
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
        this.totalTime = calculateTotalTime(); // Recalculate total time when end time changes
    }

    public LocalTime getBreakTime() {
        return breakTime;
    }

    public void setBreakTime(LocalTime breakTime) {
        this.breakTime = breakTime;
        this.totalTime = calculateTotalTime(); // Recalculate total time when break time changes
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Method to calculate total time (endTime - startTime - breakTime)
    public String calculateTotalTime() {
        if (startTime != null && endTime != null && breakTime != null) {
            Duration workDuration = Duration.between(startTime, endTime).minus(Duration.between(LocalTime.MIN, breakTime));
            long hours = workDuration.toHours();
            long minutes = workDuration.toMinutesPart();
            return String.format("%dh %dm", hours, minutes);
        }
        return "0h 0m";
    }
}