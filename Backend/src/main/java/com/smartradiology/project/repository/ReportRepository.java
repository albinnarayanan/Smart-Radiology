package com.smartradiology.project.repository;

import com.smartradiology.project.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
