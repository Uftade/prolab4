package com.education.prolab4_3;

import com.education.prolab4_3.yolcuTipi.Ogrenci;
import com.education.prolab4_3.yolcuTipi.Yasli;
import com.education.prolab4_3.yolcuTipi.Yolcu;

public class CostWeightFunction implements WeightFunction {
    @Override
    public double getWeight(Stop currentStop, NextStop edge, Stop nextStop, Yolcu yolcu) {
        double rawCost = edge.getUcret();
        if (yolcu instanceof Ogrenci) {
            return rawCost * 0.5;
        } else if (yolcu instanceof Yasli) {
            return 0.0;
        }

        return rawCost;
    }
}



