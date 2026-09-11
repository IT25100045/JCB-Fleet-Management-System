package com.jcb.jcbbookingsystem.model;

// Machine-ஓட maintenance status track பண்ண இந்த values use ஆகும்
public enum MaintenanceStatus {
    HEALTHY,           // Machine நல்ல condition-ல இருக்கு
    DUE_SOON,           // அடுத்த service date நெருங்கிடுச்சு
    UNDER_MAINTENANCE,  // இப்போ repair/service நடக்குது
    CRITICAL             // Urgent attention வேணும்
}