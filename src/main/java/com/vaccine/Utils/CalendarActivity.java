package com.vaccine.Utils;

import java.time.ZonedDateTime;

// Random Code
public class CalendarActivity {
    private ZonedDateTime date;
    private String clientName;
    private  String vaccineName;

    public CalendarActivity(ZonedDateTime date, String clientName) {
        this.date = date;
        this.clientName = clientName;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

}
