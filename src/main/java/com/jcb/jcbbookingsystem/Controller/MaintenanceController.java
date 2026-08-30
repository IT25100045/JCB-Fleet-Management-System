package com.jcb.jcbbookingsystem.Controller;

import com.jcb.jcbbookingsystem.model.MaintenanceRecord;
import com.jcb.jcbbookingsystem.model.MaintenanceStatus;
import com.jcb.jcbbookingsystem.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {

    @Autowired
    private MaintenanceService maintenanceService;

    // புது maintenance record add பண்ண
    // POST http://localhost:8080/api/maintenance
    @PostMapping
    public MaintenanceRecord addRecord(@RequestBody MaintenanceRecord record) {
        return maintenanceService.addRecord(record);
    }

    // ஒரு JCB-ஓட எல்லா records-ஐயும் பாக்க
    // GET http://localhost:8080/api/maintenance/jcb/1
    @GetMapping("/jcb/{jcbId}")
    public List<MaintenanceRecord> getRecords(@PathVariable Long jcbId) {
        return maintenanceService.getRecordsByJcbId(jcbId);
    }

    // Vehicle Health Dashboard endpoint
    // GET http://localhost:8080/api/maintenance/dashboard/1
    @GetMapping("/dashboard/{jcbId}")
    public MaintenanceStatus getDashboardStatus(@PathVariable Long jcbId) {
        return maintenanceService.getCurrentStatus(jcbId);
    }
}