package com.education.prolab4_3.responce;

import com.education.prolab4_3.request.Location;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class RouteDetail {
    @JsonProperty("type")
    private String type;

    @JsonProperty("path")
    private List<String> path;

    @JsonProperty("coordinates") // Eklenen kısım
    private List<Location> coordinates;

    @JsonProperty("fare")
    private double fare;

    @JsonProperty("duration")
    private int duration;

    @JsonProperty("transfers")
    private int transfers;

    public RouteDetail(String type, List<String> path, List<Location> coordinates, double fare, int duration, int transfers) {
        this.type = type;
        this.path = path;
        this.coordinates = coordinates;
        this.fare = fare;
        this.duration = duration;
        this.transfers = transfers;
    }

    public List<Location> getCoordinates() { return coordinates; }
}
