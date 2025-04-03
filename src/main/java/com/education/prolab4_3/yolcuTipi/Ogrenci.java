package com.education.prolab4_3.yolcuTipi;


public class Ogrenci extends Yolcu implements IndirimliBilet {
    @Override
    public double ucretHesapla(double temelUcret) {
        if (getOdemeYontemi() != null) {
            double ucret = temelUcret * getOdemeYontemi().ucretCarpani();
            if (getOdemeYontemi().indirimUygulanabilirMi()) {
                ucret *= (1 - indirimOrani());
            }
            return ucret;
        } else {
            return temelUcret;
        }
    }


    @Override
    public double indirimOrani() {
        return 0.5; // %50 indirim
    }
}
