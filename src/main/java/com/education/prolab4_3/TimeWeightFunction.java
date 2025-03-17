package com.education.prolab4_3;

import com.education.prolab4_3.yolcuTipi.Yolcu;

public class TimeWeightFunction implements WeightFunction {
    @Override
    public double getWeight(Stop currentStop, NextStop edge, Stop nextStop, Yolcu yolcu) {
        return edge.getSure();
    }
}
