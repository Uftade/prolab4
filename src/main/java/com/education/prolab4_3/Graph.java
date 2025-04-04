package com.education.prolab4_3;

import java.util.*;

public class Graph {
    private Map<String, Stop> stops = new HashMap<>();
    private Map<String, List<NextStop>> adjacency = new HashMap<>();

    public void addStop(Stop stop) {
        stops.put(stop.getId(), stop);
        adjacency.put(stop.getId(), new ArrayList<>());
    }

    public Stop getStop(String stopId) {
        return stops.get(stopId);
    }

    public List<NextStop> getNextStops(String stopId) {
        return adjacency.get(stopId);
    }

    public Map<String, Stop> getStops() {
        return stops;
    }

    public static Graph fromTransportData(TransportData data) {
        Graph g = new Graph();

        for (Stop s : data.getDuraklar()) {
            g.addStop(s);
        }

        for (Stop s : data.getDuraklar()) {
            if (s.getNextStops() != null) {
                for (NextStop ns : s.getNextStops()) {
                    g.getNextStops(s.getId()).add(ns);
                }
            }

            if (s.getTransfer() != null) {
                Transfer t = s.getTransfer();

                NextStop ns1 = new NextStop();
                ns1.setStopId(t.getTransferStopId());
                ns1.setMesafe(0.0);
                ns1.setSure(t.getTransferSure());
                ns1.setUcret(t.getTransferUcret());
                g.getNextStops(s.getId()).add(ns1);

                Stop transferStop = g.getStop(t.getTransferStopId());
                if (transferStop != null) {
                    NextStop ns2 = new NextStop();
                    ns2.setStopId(s.getId());
                    ns2.setMesafe(0.0);
                    ns2.setSure(t.getTransferSure());
                    ns2.setUcret(t.getTransferUcret());
                    g.getNextStops(transferStop.getId()).add(ns2);
                }
            }
        }

        return g;
    }

    public NextStop getEdge(String from, String to) {
        List<NextStop> edges = adjacency.get(from);
        if (edges != null) {
            for (NextStop edge : edges) {
                if (edge.getStopId().equals(to)) {
                    return edge;
                }
            }
        }
        return null;
    }
}

