package com.jcb.jcbbookingsystem.service;

import com.jcb.jcbbookingsystem.model.MaintenanceRecord;
import com.jcb.jcbbookingsystem.model.MaintenanceStatus;
import com.jcb.jcbbookingsystem.repository.MaintenanceRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MaintenanceService {

    @Autowired
    private MaintenanceRecordRepository repository;

    // புது maintenance record save பண்ண
    public MaintenanceRecord addRecord(MaintenanceRecord record) {
        record.setStatus(calculateStatus(record.getNextServiceDue()));
        return repository.save(record);
    }

    // ஒரு JCB-ஓட எல்லா records-ஐயும் எடுக்க
    public List<MaintenanceRecord> getRecordsByJcbId(Long jcbId) {
        return repository.findByJcbId(jcbId);
    }

    // Next service date-ஐ வெச்சு status calculate பண்றது (Vehicle Health Dashboard logic)
    public MaintenanceStatus calculateStatus(LocalDate nextServiceDue) {
        if (nextServiceDue == null) {
            return MaintenanceStatus.HEALTHY;
        }

        LocalDate today = LocalDate.now();
        long daysLeft = today.until(nextServiceDue).getDays();

        if (daysLeft < 0) {
            return MaintenanceStatus.CRITICAL;
        } else if (daysLeft <= 7) {
            return MaintenanceStatus.DUE_SOON;
        } else {
            return MaintenanceStatus.HEALTHY;
        }
    }

    // Dashboard-க்கு ஒரு JCB-ஓட latest status கொடுக்க
    public MaintenanceStatus getCurrentStatus(Long jcbId) {
        List<MaintenanceRecord> records = repository.findByJcbId(jcbId);
        if (records.isEmpty()) {
            return MaintenanceStatus.HEALTHY;
        }
        MaintenanceRecord latest = records.get(records.size() - 1);
        return calculateStatus(latest.getNextServiceDue());
    }
}