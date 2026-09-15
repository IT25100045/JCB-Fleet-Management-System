package com.jcb.jcbbookingsystem.Controller;

import com.jcb.jcbbookingsystem.dto.DriverDTO;
import com.jcb.jcbbookingsystem.model.DriverStatus;
import com.jcb.jcbbookingsystem.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {
    private final DriverService driverService;

    @PostMapping
    public ResponseEntity<DriverDTO> createDriver(
            @RequestBody DriverDTO driverDTO) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(driverService.createDriver(driverDTO));
    }

    @GetMapping
    public ResponseEntity<List<DriverDTO>> getAllDrivers() {

        return ResponseEntity.ok(
                driverService.getAllDrivers()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverDTO> getDriverById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                driverService.getDriverById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<DriverDTO> updateDriver(
            @PathVariable Long id,
            @RequestBody DriverDTO driverDTO) {

        return ResponseEntity.ok(
                driverService.updateDriver(id, driverDTO)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(
            @PathVariable Long id) {

        driverService.deleteDriver(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<DriverDTO>> getDriversByStatus(
            @PathVariable DriverStatus status) {

        return ResponseEntity.ok(
                driverService.getDriversByStatus(status)
        );
    }

    @GetMapping("/available")
    public ResponseEntity<List<DriverDTO>> getAvailableDrivers(
            @RequestParam LocalDate date,
            @RequestParam LocalTime startTime,
            @RequestParam LocalTime endTime) {

        return ResponseEntity.ok(
                driverService.getAvailableDrivers(
                        date,
                        startTime,
                        endTime
                )
        );
    }

}
