package com.education.prolab4_3.araclar;

import com.education.prolab4_3.odemeYontemleri.KrediKart;
import com.education.prolab4_3.odemeYontemleri.Nakit;
import com.education.prolab4_3.odemeYontemleri.OdemeYontemi;
import com.education.prolab4_3.yolcuTipi.Yolcu;

public class Taksi extends Arac {

    private double openingFee;
    private double costPerKm;

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
        // Yolcunun ödeme yöntemi alınır.

        OdemeYontemi odeme = yolcu.getOdemeYontemi();
        // Eğer ödeme yöntemi Nakit veya KrediKart değilse hata fırlatılır.
        if (!(odeme instanceof Nakit || odeme instanceof KrediKart)) {
            throw new IllegalArgumentException("Taksi ödemesi sadece nakit veya kredi kartı ile yapılabilir.");
        }
        // Taksi için indirim uygulanmaz; temel ücret döndürülür.
        return temelUcret;
    }
}
