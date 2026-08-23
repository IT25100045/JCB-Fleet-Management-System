package com.jcb.jcbbookingsystem.Controller;

import com.jcb.jcbbookingsystem.dto.DriverDTO;
import com.jcb.jcbbookingsystem.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @GetMapping
    public ResponseEntity<List<DriverDTO>> getAll(
            @RequestParam(required = false, defaultValue = "false") boolean availableOnly) {
        return ResponseEntity.ok(availableOnly ? driverService.getAvailable() : driverService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(driverService.getById(id));
    }

    @PostMapping
    public ResponseEntity<DriverDTO> create(@Valid @RequestBody DriverDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(driverService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DriverDTO> update(@PathVariable Long id, @Valid @RequestBody DriverDTO dto) {
        return ResponseEntity.ok(driverService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        driverService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

