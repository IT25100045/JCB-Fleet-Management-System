package com.jcb.jcbbookingsystem.service;

import com.jcb.jcbbookingsystem.model.FuelRecord;
import com.jcb.jcbbookingsystem.repository.FuelRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuelService {

    @Autowired
    private FuelRecordRepository repository;

    // புது fuel record save பண்ண
    public FuelRecord addRecord(FuelRecord record) {
        return repository.save(record);
    }

    // ஒரு JCB-ஓட எல்லா fuel records-ஐயும் எடுக்க
    public List<FuelRecord> getRecordsByJcbId(Long jcbId) {
        return repository.findByJcbId(jcbId);
    }

    // Fuel efficiency calculate பண்ண (liters per reading unit)
    // உதாரணமா: total liters / total distance/hours
    public double calculateAverageConsumption(Long jcbId) {
        List<FuelRecord> records = repository.findByJcbId(jcbId);
        if (records.isEmpty()) {
            return 0.0;
        }

        double totalLiters = 0;
        for (FuelRecord record : records) {
            totalLiters += record.getLitersFilled();
        }

        return totalLiters / records.size(); // Average liters per entry
    }
}