package com.jcb.jcbbookingsystem.repository;

import com.jcb.jcbbookingsystem.model.IncidentReport;
import com.jcb.jcbbookingsystem.model.IncidentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidentReportRepository extends JpaRepository<IncidentReport, Long> {

    List<IncidentReport> findByJcbId(Long jcbId);

    List<IncidentReport> findByOperatorUsername(String operatorUsername);

    List<IncidentReport> findByStatus(IncidentStatus status);
}