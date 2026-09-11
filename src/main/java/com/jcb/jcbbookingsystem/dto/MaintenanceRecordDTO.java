package com.jcb.jcbbookingsystem.dto;

import com.jcb.jcbbookingsystem.model.MaintenanceStatus;
import java.time.LocalDate;

public class MaintenanceRecordDTO {

    private Long jcbId;
    private LocalDate serviceDate;
    private String serviceType;
    private String description;
    private Double cost;
    private MaintenanceStatus status;
    private LocalDate nextServiceDue;

    // Getters and Setters
    public Long getJcbId() { return jcbId; }
    public void setJcbId(Long jcbId) { this.jcbId = jcbId; }

    public LocalDate getServiceDate() { return serviceDate; }
    public void setServiceDate(LocalDate serviceDate) { this.serviceDate = serviceDate; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }

    public MaintenanceStatus getStatus() { return status; }
    public void setStatus(MaintenanceStatus status) { this.status = status; }

    public LocalDate getNextServiceDue() { return nextServiceDue; }
    public void setNextServiceDue(LocalDate nextServiceDue) { this.nextServiceDue = nextServiceDue; }
}