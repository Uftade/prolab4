package com.education.prolab4_3.request;

public class RouteRequest {
    private String passengerType;
    private String paymentMethod;
    private Location start;
    private Location end;




    // Getter'lar
    public String getPassengerType() {
        return passengerType;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public Location getStart() {
        return start;
    }

    public Location getEnd() {
        return end;
    }

    // Setter'lar
    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public void setEnd(Location end) {
        this.end = end;
    }
}
