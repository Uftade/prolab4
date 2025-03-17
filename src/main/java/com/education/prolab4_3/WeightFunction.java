package com.education.prolab4_3;

import com.education.prolab4_3.yolcuTipi.Yolcu;

public interface WeightFunction {
    double getWeight(Stop currentStop, NextStop edge, Stop nextStop, Yolcu yolcu);
}

