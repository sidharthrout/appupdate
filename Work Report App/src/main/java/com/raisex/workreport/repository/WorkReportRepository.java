package com.raisex.workreport.repository;

import com.raisex.workreport.model.WorkReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkReportRepository extends JpaRepository<WorkReport, Long> {

    @Query("SELECT wr FROM WorkReport wr WHERE YEAR(wr.reportDate) = :year AND MONTH(wr.reportDate) = :month")
    List<WorkReport> findByYearAndMonth(@Param("year") int year, @Param("month") int month);
}