package com.education.prolab4_3.yolcuTipi;

import com.education.prolab4_3.odemeYontemleri.OdemeYontemi;

public abstract class Yolcu {
    // Ödeme yöntemi bilgisi
    private OdemeYontemi odemeYontemi;

    // Ödeme yöntemi set metodu
    public void setOdemeYontemi(OdemeYontemi odemeYontemi) {
        this.odemeYontemi = odemeYontemi;
    }

    public OdemeYontemi getOdemeYontemi() {
        return this.odemeYontemi;
    }

    // Ücret hesaplama metodu
    public abstract double ucretHesapla(double temelUcret);
}
