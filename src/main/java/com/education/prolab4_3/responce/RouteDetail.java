package com.education.prolab4_3.responce;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class RouteDetail {
    @JsonProperty("type")
    private String type;

    @JsonProperty("path")
    private List<String> path;

    @JsonProperty("fare")
    private double fare;

    @JsonProperty("duration")
    private int duration;

    @JsonProperty("transfers")
    private int transfers;

    public RouteDetail(String type, List<String> path, double fare, int duration, int transfers) {
        this.type = type;
        this.path = path;
        this.fare = fare;
        this.duration = duration;
        this.transfers = transfers;
    }

    public String getType() { return type; }
    public List<String> getPath() { return path; }
    public double getFare() { return fare; }
    public int getDuration() { return duration; }
    public int getTransfers() { return transfers; }
}