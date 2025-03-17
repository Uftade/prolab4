package com.education.prolab4_3.yolcuTipi;


public class Yasli extends Yolcu implements IndirimliBilet {
    @Override
    public double ucretHesapla(double temelUcret) {
        if (getOdemeYontemi() != null && getOdemeYontemi().indirimUygulanabilirMi()) {
            return temelUcret * (1 - indirimOrani());
        } else {
            return temelUcret;
        }
    }

    @Override
    public double indirimOrani() {
        return 1.0; // %100 indirim, yani ücret 0 TL
    }
}
