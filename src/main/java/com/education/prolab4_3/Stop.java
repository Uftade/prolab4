package com.education.prolab4_3;

import java.util.List;

public class Stop {
    private String id;
    private String name;
    private String type;    // "bus" veya "tram"
    private double lat;
    private double lon;
    private boolean sonDurak;
    private List<NextStop> nextStops;
    private Transfer transfer;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<NextStop> getNextStops() {
        return nextStops;
    }

    public void setNextStops(List<NextStop> nextStops) {
        this.nextStops = nextStops;
    }

    public boolean isSonDurak() {
        return sonDurak;
    }

    public void setSonDurak(boolean sonDurak) {
        this.sonDurak = sonDurak;
    }

    public Transfer getTransfer() {
        return transfer;
    }

    public void setTransfer(Transfer transfer) {
        this.transfer = transfer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
