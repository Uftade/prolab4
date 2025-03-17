package com.education.prolab4_3.yolcuTipi;


public class Ogrenci extends Yolcu implements IndirimliBilet {
    @Override
    public double ucretHesapla(double temelUcret) {
        // Eğer ödeme yöntemi indirim uygulanabilir ise indirim uygula, aksi halde temel ücreti döndür
        if (getOdemeYontemi() != null && getOdemeYontemi().indirimUygulanabilirMi()) {
            return temelUcret * (1 - indirimOrani());
        } else {
            return temelUcret;
        }
    }

    @Override
    public double indirimOrani() {
        return 0.5; // %50 indirim
    }
}
