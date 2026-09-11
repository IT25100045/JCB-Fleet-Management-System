package com.jcb.jcbbookingsystem.Controller;

import com.jcb.jcbbookingsystem.model.FuelRecord;
import com.jcb.jcbbookingsystem.service.FuelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fuel")
public class FuelController {

    @Autowired
    private FuelService fuelService;

    // புது fuel record add பண்ண
    // POST http://localhost:8080/api/fuel
    @PostMapping
    public FuelRecord addRecord(@RequestBody FuelRecord record) {
        return fuelService.addRecord(record);
    }

    // ஒரு JCB-ஓட எல்லா fuel records-ஐயும் பாக்க
    // GET http://localhost:8080/api/fuel/jcb/1
    @GetMapping("/jcb/{jcbId}")
    public List<FuelRecord> getRecords(@PathVariable Long jcbId) {
        return fuelService.getRecordsByJcbId(jcbId);
    }

    // Average fuel consumption பாக்க
    // GET http://localhost:8080/api/fuel/average/1
    @GetMapping("/average/{jcbId}")
    public double getAverageConsumption(@PathVariable Long jcbId) {
        return fuelService.calculateAverageConsumption(jcbId);
    }
}