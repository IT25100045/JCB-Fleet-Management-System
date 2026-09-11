package com.jcb.jcbbookingsystem.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "maintenance_records")
public class MaintenanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                     // ஒவ்வொரு record-க்கும் unique ID

    private Long jcbId;                  // எந்த JCB machine-க்கு இந்த record

    private LocalDate serviceDate;       // எப்போ service பண்ணது

    private String serviceType;          // என்ன மாதிரி service (Oil change, Engine repair, etc.)

    private String description;          // Details

    private Double cost;                 // Repair/service-க்கு ஆன செலவு

    @Enumerated(EnumType.STRING)
    private MaintenanceStatus status;    // Current status (HEALTHY, DUE_SOON, etc.)

    private LocalDate nextServiceDue;    // அடுத்த service எப்போ வேணும்

    // ---- Getters and Setters ----

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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