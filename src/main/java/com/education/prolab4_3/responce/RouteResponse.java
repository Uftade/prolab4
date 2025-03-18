package com.education.prolab4_3.responce;

import java.util.List;

public class RouteResponse {
    private List<RouteDetail> routes;

    public RouteResponse(List<RouteDetail> routes) {
        this.routes = routes;
    }

    public List<RouteDetail> getRoutes() { return routes; }
}