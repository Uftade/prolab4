package com.education.prolab4_3.responce;

import java.util.List;

public class RouteResponse {
    private List<RouteDetail> routes;
    private String nearestStartStopName;
    private String nearestEndStopName;
    private double distanceToStartStop;
    private double distanceToEndStop;
    private String startMessage;
    private String endMessage;

    public RouteResponse(List<RouteDetail> routes,
                         String nearestStartStopName,
                         double distanceToStartStop,
                         String startMessage,
                         String nearestEndStopName,
                         double distanceToEndStop,
                         String endMessage) {
        this.routes = routes;
        this.nearestStartStopName = nearestStartStopName;
        this.distanceToStartStop = distanceToStartStop;
        this.startMessage = startMessage;
        this.nearestEndStopName = nearestEndStopName;
        this.distanceToEndStop = distanceToEndStop;
        this.endMessage = endMessage;
    }

    // Getter Metotları
    public List<RouteDetail> getRoutes() { return routes; }
    public String getNearestStartStopName() { return nearestStartStopName; }
    public double getDistanceToStartStop() { return distanceToStartStop; }
    public String getStartMessage() { return startMessage; }
    public String getNearestEndStopName() { return nearestEndStopName; }
    public double getDistanceToEndStop() { return distanceToEndStop; }
    public String getEndMessage() { return endMessage; }
}
