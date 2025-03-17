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
    private static final double EARTH_RADIUS = 6371.0; // km

    public static void main(String[] args) {
        SpringApplication.run(Prolab43Application.class, args);
        Scanner scanner = new Scanner(System.in);

        // 1) Yolcu tipi girilmesi
        System.out.println("Yolcu tipini giriniz (ogrenci / yasli / yetiskin): ");
        String yolcuTipi = scanner.nextLine().toLowerCase();

        Yolcu yolcu = switch (yolcuTipi) {
            case "ogrenci" -> new Ogrenci();
            case "yasli" -> new Yasli();
            default -> new Yetiskin();
        };

        // 2) Kullanıcının bulunduğu konumun (lat, lon) alınması
        System.out.println("Lütfen bulunduğunuz konumun enlem (lat) değerini girin:");
        double kullaniciLat = Double.parseDouble(scanner.nextLine().replace(",", "."));
        System.out.println("Lütfen bulunduğunuz konumun boylam (lon) değerini girin:");
        double kullaniciLon = Double.parseDouble(scanner.nextLine().replace(",", "."));

        // 3) Hedef konumun (lat, lon) alınması
        System.out.println("Gitmek istediğiniz konumun enlem (lat) değerini girin:");
        double hedefLat = Double.parseDouble(scanner.nextLine().replace(",", "."));
        System.out.println("Gitmek istediğiniz konumun boylam (lon) değerini girin:");
        double hedefLon = Double.parseDouble(scanner.nextLine().replace(",", "."));

        scanner.close();

        // 4) JSON dosyasını okuyup, grafı oluşturuyoruz
        TransportData data = JsonLoader.load("C:\\Users\\murat2\\java_projeler\\prolab4_3\\src\\main\\resources\\RawData.json");
        Graph graph = Graph.fromTransportData(data);

        // 5) Girilen koordinatlara en yakın durakları Haversine formülü ile buluyoruz
        Stop startStop = findNearestStop(graph, kullaniciLat, kullaniciLon);
        Stop goalStop  = findNearestStop(graph, hedefLat, hedefLon);

        double startDistance = haversineDistance(kullaniciLat, kullaniciLon, startStop.getLat(), startStop.getLon());
        double goalDistance  = haversineDistance(hedefLat, hedefLon, goalStop.getLat(), goalStop.getLon());

        System.out.println("Başlangıç için en yakın durak: " + startStop.getName() + " (mesafe: "
                + String.format("%.2f", startDistance) + " km)");
        System.out.println("Hedef için en yakın durak: " + goalStop.getName() + " (mesafe: "
                + String.format("%.2f", goalDistance) + " km)");

        // 3 km kuralı: Eğer mesafe 3 km'den fazla ise uyarı verelim ve tahmini taksi ücretini hesaplayalım
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


        // 6) Dijkstra algoritması ile üç farklı rota hesaplaması
        List<String> enUcuz = Dijkstra.findPath(graph, startStop.getId(), goalStop.getId(), new CostWeightFunction(), yolcu);
        List<String> enKisa = Dijkstra.findPath(graph, startStop.getId(), goalStop.getId(), new TimeWeightFunction(), yolcu);
        List<String> enAzAktarma = Dijkstra.findPath(graph, startStop.getId(), goalStop.getId(), new TransferWeightFunction(), yolcu);

        // 7) Rota detaylarını hesaplayalım
        double totalCost = calculateTotalCost(graph, enUcuz, yolcu);
        int totalTime = calculateTotalTime(graph, enKisa);
        int totalTransfers = calculateTotalTransfers(graph, enAzAktarma);

        // 8) Taksi ile direkt ulaşım (en hızlı rota) hesaplaması
        double taxiDistance = haversineDistance(kullaniciLat, kullaniciLon, hedefLat, hedefLon);
        // Ortalama 40 km/saat hız varsayımı: yaklaşık 1.5 dk/km
        int taxiTime = (int) Math.round(taxiDistance * 1.5);
        double taxiFare = data.getTaxi().getOpeningFee()
                + data.getTaxi().getCostPerKm() * taxiDistance;

        // 9) Sonuçları ekrana yazdırma
        System.out.println("En Ucuz Rota: " + enUcuz + " | Toplam Ücret: " + totalCost + " TL");
        System.out.println("En Kısa Rota: " + enKisa + " | Toplam Süre: " + totalTime + " dk");
        System.out.println("En Az Aktarmalı Rota: " + enAzAktarma + " | Aktarma Sayısı: " + totalTransfers);
        System.out.println("En Hızlı Rota (Taksi): [Direkt Taksi]"
                + " | Mesafe: " + String.format("%.2f", taxiDistance) + " km"
                + " | Tahmini Süre: " + taxiTime + " dk"
                + " | Tahmini Ücret: " + String.format("%.2f", taxiFare) + " TL");
    }

    // Haversine formülü ile iki koordinat arasındaki km cinsinden mesafe hesaplama
    public static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    // Graph içerisindeki tüm duraklar arasında girilen konuma en yakın olanı bulur
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

    // Rota üzerindeki toplam ücreti hesaplar
    private static double calculateTotalCost(Graph graph, List<String> path, Yolcu yolcu) {
        double totalCost = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            NextStop edge = graph.getEdge(path.get(i), path.get(i + 1));
            if (edge != null) {
                double cost = edge.getUcret();
                // Yolcu tipine göre indirim uygulaması
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

    // Rota üzerindeki toplam süreyi hesaplar (dakika cinsinden)
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

    // Rota üzerindeki toplam aktarma sayısını hesaplar (her geçiş 1 transfer sayılır)
    private static int calculateTotalTransfers(Graph graph, List<String> path) {
        return path.size() - 1;
    }

}
