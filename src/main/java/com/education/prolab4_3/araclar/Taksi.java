package com.education.prolab4_3.araclar;
import com.education.prolab4_3.odemeYontemleri.KrediKart;
import com.education.prolab4_3.odemeYontemleri.Nakit;
import com.education.prolab4_3.odemeYontemleri.OdemeYontemi;
import com.education.prolab4_3.yolcuTipi.Yolcu;

public class Taksi extends Arac {

    private double openingFee = 10.0; // Açılış ücreti
    private double costPerKm = 5.0;

    public double getCostPerKm() {
        return costPerKm;
    }

    public void setCostPerKm(double costPerKm) {
        this.costPerKm = costPerKm;
    }

    public double getOpeningFee() {
        return openingFee;
    }

    public void setOpeningFee(double openingFee) {
        this.openingFee = openingFee;
    }

    @Override
    public double fiyatHesapla(double temelUcret, Yolcu yolcu) {

        OdemeYontemi odeme = yolcu.getOdemeYontemi();
        if (!(odeme instanceof Nakit || odeme instanceof KrediKart)) {
            throw new IllegalArgumentException("Taksi ödemesi sadece nakit veya kredi kartı ile yapılabilir.");
        }
        return temelUcret;
    }
}