package com.education.prolab4_3.araclar;

import com.education.prolab4_3.yolcuTipi.Yolcu;

public class Otobus extends Arac {
    @Override
    public double fiyatHesapla(double temelUcret, Yolcu yolcu) {
        return yolcu.ucretHesapla(temelUcret);
    }
}
