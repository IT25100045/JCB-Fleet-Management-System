package com.jcb.jcbbookingsystem.Controller;

import com.jcb.jcbbookingsystem.dto.FuelRecordDTO;
import com.jcb.jcbbookingsystem.service.FuelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fuel")
@RequiredArgsConstructor
public class FuelController {

    private final FuelService fuelService;

    @GetMapping
    public ResponseEntity<List<FuelRecordDTO>> getAll() {
        return ResponseEntity.ok(fuelService.getAll());
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<FuelRecordDTO>> getByVehicle(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(fuelService.getByVehicle(vehicleId));
    }

    @PostMapping
    public ResponseEntity<FuelRecordDTO> create(@Valid @RequestBody FuelRecordDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fuelService.create(dto));
    }
}

