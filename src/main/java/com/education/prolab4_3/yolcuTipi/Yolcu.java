package com.education.prolab4_3.yolcuTipi;

import com.education.prolab4_3.odemeYontemleri.OdemeYontemi;

public abstract class Yolcu {
    private OdemeYontemi odemeYontemi;

    public void setOdemeYontemi(OdemeYontemi odemeYontemi) {
        this.odemeYontemi = odemeYontemi;
    }

    public OdemeYontemi getOdemeYontemi() {
        return this.odemeYontemi;
    }

    public abstract double ucretHesapla(double temelUcret);
}
