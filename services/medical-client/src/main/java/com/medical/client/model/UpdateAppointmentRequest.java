package com.medical.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateAppointmentRequest {
    private String date;
    private String time;

    public UpdateAppointmentRequest() {}

    public UpdateAppointmentRequest(String date, String time) {
        this.date = date;
        this.time = time;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}
