package com.education.prolab4_3;

import com.education.prolab4_3.yolcuTipi.Yolcu;

import java.util.*;

public class Dijkstra {
    public static List<String> findPath(Graph graph, String startId, String goalId,
                                        WeightFunction wf, Yolcu yolcu) {
        Map<String, Double> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();

        for (String sid : graph.getStops().keySet()) {
            dist.put(sid, Double.POSITIVE_INFINITY);
            prev.put(sid, null);
        }
        dist.put(startId, 0.0);

        PriorityQueue<String> pq = new PriorityQueue<>((a, b) -> dist.get(a).compareTo(dist.get(b)));
        pq.add(startId);

        while (!pq.isEmpty()) {
            String current = pq.poll();
            if (current.equals(goalId)) break;

            List<NextStop> edges = graph.getNextStops(current);
            if (edges == null) continue;

            for (NextStop edge : edges) {
                Stop curStopObj = graph.getStop(current);
                Stop nextStopObj = graph.getStop(edge.getStopId());
                double cost = wf.getWeight(curStopObj, edge, nextStopObj, yolcu);
                double alt = dist.get(current) + cost;
                if (alt < dist.get(edge.getStopId())) {
                    dist.put(edge.getStopId(), alt);
                    prev.put(edge.getStopId(), current);
                    pq.remove(edge.getStopId());
                    pq.add(edge.getStopId());
                }
            }
        }
        List<String> path = new ArrayList<>();
        String node = goalId;
        while (node != null) {
            path.add(0, node);
            node = prev.get(node);
        }
        if (!path.isEmpty() && !path.get(0).equals(startId)) {
            return Collections.emptyList();
        }
        return path;
    }
}

