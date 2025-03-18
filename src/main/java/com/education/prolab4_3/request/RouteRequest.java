package com.education.prolab4_3.request;

public class RouteRequest {
    private String passengerType;
    private String paymentMethod;
    private Location start;
    private Location end;

    public String getPassengerType() { return passengerType; }
    public String getPaymentMethod() { return paymentMethod; }
    public Location getStart() { return start; }
    public Location getEnd() { return end; }
}