package com.education.prolab4_3.controller;

import com.education.prolab4_3.*;
import com.education.prolab4_3.araclar.Taksi;
import com.education.prolab4_3.request.Location;
import com.education.prolab4_3.request.RouteRequest;
import com.education.prolab4_3.responce.RouteDetail;
import com.education.prolab4_3.responce.RouteResponse;
import com.education.prolab4_3.yolcuTipi.Ogrenci;
import com.education.prolab4_3.yolcuTipi.Yasli;
import com.education.prolab4_3.yolcuTipi.Yetiskin;
import com.education.prolab4_3.yolcuTipi.Yolcu;
import com.education.prolab4_3.odemeYontemleri.KrediKart;
import com.education.prolab4_3.odemeYontemleri.Nakit;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/routes")
@CrossOrigin(origins = "http://localhost:3000") // React ile çalışabilmesi için CORS izni
public class RouteController {

    private final Graph graph;

    public RouteController() {
        TransportData data = JsonLoader.load("C:\\Users\\murat2\\java_projeler\\prolab4_3\\src\\main\\resources\\RawData.json");
        this.graph = Graph.fromTransportData(data);
    }

    @PostMapping
    public RouteResponse getRoute(@RequestBody RouteRequest request) {
        Yolcu yolcu;
        switch (request.getPassengerType().toLowerCase()) {
            case "ogrenci" -> yolcu = new Ogrenci();
            case "yasli" -> yolcu = new Yasli();
            default -> yolcu = new Yetiskin();
        }

        if (!(yolcu.getOdemeYontemi() instanceof Nakit || yolcu.getOdemeYontemi() instanceof KrediKart)) {
            yolcu.setOdemeYontemi(new Nakit());
        }

        Stop startStop = findNearestStop(graph, request.getStart().getLat(), request.getStart().getLon());
        Stop endStop = findNearestStop(graph, request.getEnd().getLat(), request.getEnd().getLon());

        double startDistance = haversineDistance(request.getStart().getLat(), request.getStart().getLon(), startStop.getLat(), startStop.getLon());
        double endDistance = haversineDistance(request.getEnd().getLat(), request.getEnd().getLon(), endStop.getLat(), endStop.getLon());

        String startMessage = startDistance > 3.0 ?
                "Başlangıç noktanız 3 km'den fazla uzak, taksi kullanmanız gerekebilir. Tahmini taksi ücreti: " + String.format("%.2f", (10 + 5 * startDistance)) + " TL"
                : "Başlangıç noktanız 3 km'den az, yürüyebilirsiniz.";

        String endMessage = endDistance > 3.0 ?
                "Hedef noktanız 3 km'den fazla uzak, taksi kullanmanız gerekebilir. Tahmini taksi ücreti: " + String.format("%.2f", (10 + 5 * endDistance)) + " TL"
                : "Hedef noktanız 3 km'den az, yürüyebilirsiniz.";

        List<String> enUcuz = Dijkstra.findPath(graph, startStop.getId(), endStop.getId(), new CostWeightFunction(), yolcu);
        List<String> enKisa = Dijkstra.findPath(graph, startStop.getId(), endStop.getId(), new TimeWeightFunction(), yolcu);
        List<String> enAzAktarma = Dijkstra.findPath(graph, startStop.getId(), endStop.getId(), new TransferWeightFunction(), yolcu);

        double mesafe = haversineDistance(request.getStart().getLat(), request.getStart().getLon(), request.getEnd().getLat(), request.getEnd().getLon());

        Taksi taksi = new Taksi();
        double taksiUcreti = taksi.fiyatHesapla(taksi.getOpeningFee() + (taksi.getCostPerKm() * mesafe), yolcu);
        int taksiSuresi = (int) (mesafe * 2);

        RouteDetail taksiAlternatif = new RouteDetail(
                "En Hızlı Rota (Taksi)",
                List.of("Başlangıç", "Taksi ile Varış"),
                List.of(
                        new Location(request.getStart().getLat(), request.getStart().getLon()),
                        new Location(request.getEnd().getLat(), request.getEnd().getLon())
                ),
                taksiUcreti,
                taksiSuresi,
                0
        );

        return new RouteResponse(
                List.of(
                        taksiAlternatif,
                        new RouteDetail("En Ucuz Rota", enUcuz, getCoordinatesFromPath(graph, enUcuz), calculateTotalCost(graph, enUcuz, yolcu), calculateTotalTime(graph, enUcuz), calculateTotalTransfers(graph, enUcuz)),
                        new RouteDetail("En Kısa Rota", enKisa, getCoordinatesFromPath(graph, enKisa), calculateTotalCost(graph, enKisa, yolcu), calculateTotalTime(graph, enKisa), calculateTotalTransfers(graph, enKisa)),
                        new RouteDetail("En Az Aktarma", enAzAktarma, getCoordinatesFromPath(graph, enAzAktarma), calculateTotalCost(graph, enAzAktarma, yolcu), calculateTotalTime(graph, enAzAktarma), calculateTotalTransfers(graph, enAzAktarma))
                ),
                startStop.getName(),
                startDistance,
                startMessage,
                endStop.getName(),
                endDistance,
                endMessage
        );
    }


    private List<Location> getCoordinatesFromPath(Graph graph, List<String> path) {
        List<Location> coordinates = new ArrayList<>();
        for (String stopId : path) {
            Stop stop = graph.getStop(stopId);
            coordinates.add(new Location(stop.getLat(), stop.getLon()));
        }
        return coordinates;
    }

    private Stop findNearestStop(Graph graph, double lat, double lon) {
        Stop nearest = null;
        double minDist = Double.POSITIVE_INFINITY;
        for (Stop s : graph.getStops().values()) {
            double d = haversineDistance(lat, lon, s.getLat(), s.getLon());
            if (d < minDist) {
                minDist = d;
                nearest = s;
            }
        }
        return nearest;
    }

    private double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final double EARTH_RADIUS = 6371.0; // km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    private static double calculateTotalCost(Graph graph, List<String> path, Yolcu yolcu) {
        double totalCost = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            NextStop edge = graph.getEdge(path.get(i), path.get(i + 1));
            if (edge != null) {
                double cost = edge.getUcret();
                if (yolcu instanceof Ogrenci) {
                    cost *= (1 - ((Ogrenci) yolcu).indirimOrani());
                } else if (yolcu instanceof Yasli) {
                    cost = 0.0;
                }
                totalCost += cost;
            }
        }
        return totalCost;
    }

    private static int calculateTotalTime(Graph graph, List<String> path) {
        int totalTime = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            NextStop edge = graph.getEdge(path.get(i), path.get(i + 1));
            if (edge != null) {
                totalTime += edge.getSure();
            }
        }
        return totalTime;
    }

    private static int calculateTotalTransfers(Graph graph, List<String> path) {
        return path.size() - 1;
    }
}