package com.education.prolab4_3;

import com.education.prolab4_3.yolcuTipi.Yolcu;

public class TransferWeightFunction implements WeightFunction {
    @Override
    public double getWeight(Stop currentStop, NextStop edge, Stop nextStop, Yolcu yolcu) {
        // Her geçiş için sabit 1 döndürür
        return 1;
    }
}
