package com.education.prolab4_3;

import com.education.prolab4_3.yolcuTipi.Ogrenci;
import com.education.prolab4_3.yolcuTipi.Yasli;
import com.education.prolab4_3.yolcuTipi.Yetiskin;
import com.education.prolab4_3.yolcuTipi.Yolcu;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class Prolab43Application {
    private static final double EARTH_RADIUS = 6371.0;

    public static void main(String[] args) {
        SpringApplication.run(Prolab43Application.class, args);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Yolcu tipini giriniz (ogrenci / yasli / yetiskin): ");
        String yolcuTipi = scanner.nextLine().toLowerCase();

        Yolcu yolcu = switch (yolcuTipi) {
            case "ogrenci" -> new Ogrenci();
            case "yasli" -> new Yasli();
            default -> new Yetiskin();
        };

        System.out.println("Lütfen bulunduğunuz konumun enlem (lat) değerini girin:");
        double kullaniciLat = Double.parseDouble(scanner.nextLine().replace(",", "."));
        System.out.println("Lütfen bulunduğunuz konumun boylam (lon) değerini girin:");
        double kullaniciLon = Double.parseDouble(scanner.nextLine().replace(",", "."));

        System.out.println("Gitmek istediğiniz konumun enlem (lat) değerini girin:");
        double hedefLat = Double.parseDouble(scanner.nextLine().replace(",", "."));
        System.out.println("Gitmek istediğiniz konumun boylam (lon) değerini girin:");
        double hedefLon = Double.parseDouble(scanner.nextLine().replace(",", "."));

        scanner.close();

        TransportData data = JsonLoader.load("C:\\Users\\murat2\\java_projeler\\prolab4_3\\src\\main\\resources\\RawData.json");
        Graph graph = Graph.fromTransportData(data);

        Stop startStop = findNearestStop(graph, kullaniciLat, kullaniciLon);
        Stop goalStop  = findNearestStop(graph, hedefLat, hedefLon);

        double startDistance = haversineDistance(kullaniciLat, kullaniciLon, startStop.getLat(), startStop.getLon());
        double goalDistance  = haversineDistance(hedefLat, hedefLon, goalStop.getLat(), goalStop.getLon());

        System.out.println("Başlangıç için en yakın durak: " + startStop.getName() + " (mesafe: "
                + String.format("%.2f", startDistance) + " km)");
        System.out.println("Hedef için en yakın durak: " + goalStop.getName() + " (mesafe: "
                + String.format("%.2f", goalDistance) + " km)");

        if (startDistance > 3.0) {
            double taxiFareStart = data.getTaxi().getOpeningFee()
                    + data.getTaxi().getCostPerKm() * startDistance;
            System.out.println("Uyarı: Başlangıç noktanız 3 km'den fazla uzak, taksi kullanmanız gerekebilir. " +
                    "Tahmini taksi ücreti: " + String.format("%.2f", taxiFareStart) + " TL");
        } else if (startDistance < 3.0 && startDistance >0){
            System.out.println("Uyarı: Başlangıç noktanız 3 km'den az, yürüyebilirsiniz.");
        }
        else {
            System.out.println("Zaten başlangıç durağındasınız burada bakleyebilirsiniz.");
        }

        if (goalDistance > 3.0) {
            double taxiFareGoal = data.getTaxi().getOpeningFee()
                    + data.getTaxi().getCostPerKm() * goalDistance;
            System.out.println("Uyarı: Hedef noktanız 3 km'den fazla uzak, taksi kullanmanız gerekebilir. " +
                    "Tahmini taksi ücreti: " + String.format("%.2f", taxiFareGoal) + " TL");
        } else if (goalDistance < 3.0 && goalDistance >0) {
            System.out.println("Uyarı: Hedef noktanız 3 km'den az, yürüyebilirsiniz.");
        }
        else {
            System.out.println("Zaten hedef noktanızdasınız geçmiş olsun.");
        }


        List<String> enUcuz = Dijkstra.findPath(graph, startStop.getId(), goalStop.getId(), new CostWeightFunction(), yolcu);
        List<String> enKisa = Dijkstra.findPath(graph, startStop.getId(), goalStop.getId(), new TimeWeightFunction(), yolcu);
        List<String> enAzAktarma = Dijkstra.findPath(graph, startStop.getId(), goalStop.getId(), new TransferWeightFunction(), yolcu);

        double totalCost = calculateTotalCost(graph, enUcuz, yolcu);
        int totalTime = calculateTotalTime(graph, enKisa);
        int totalTransfers = calculateTotalTransfers(graph, enAzAktarma);

        double taxiDistance = haversineDistance(kullaniciLat, kullaniciLon, hedefLat, hedefLon);
        int taxiTime = (int) Math.round(taxiDistance * 1.5);
        double taxiFare = data.getTaxi().getOpeningFee()
                + data.getTaxi().getCostPerKm() * taxiDistance;

        System.out.println("En Ucuz Rota: " + enUcuz + " | Toplam Ücret: " + totalCost + " TL");
        System.out.println("En Kısa Rota: " + enKisa + " | Toplam Süre: " + totalTime + " dk");
        System.out.println("En Az Aktarmalı Rota: " + enAzAktarma + " | Aktarma Sayısı: " + totalTransfers);
        System.out.println("En Hızlı Rota (Taksi): [Direkt Taksi]"
                + " | Mesafe: " + String.format("%.2f", taxiDistance) + " km"
                + " | Tahmini Süre: " + taxiTime + " dk"
                + " | Tahmini Ücret: " + String.format("%.2f", taxiFare) + " TL");
    }

    public static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    private static Stop findNearestStop(Graph graph, double userLat, double userLon) {
        Stop nearest = null;
        double minDist = Double.POSITIVE_INFINITY;
        for (Stop s : graph.getStops().values()) {
            double d = haversineDistance(userLat, userLon, s.getLat(), s.getLon());
            if (d < minDist) {
                minDist = d;
                nearest = s;
            }
        }
        return nearest;
    }

    private static double calculateTotalCost(Graph graph, List<String> path, Yolcu yolcu) {
        double totalCost = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            NextStop edge = graph.getEdge(path.get(i), path.get(i + 1));
            if (edge != null) {
                double cost = edge.getUcret();
                if (yolcu instanceof Ogrenci) {
                    cost *= ((Ogrenci) yolcu).indirimOrani();
                } else if (yolcu instanceof Yasli) {
                    cost = ((Yasli) yolcu).indirimOrani();
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
