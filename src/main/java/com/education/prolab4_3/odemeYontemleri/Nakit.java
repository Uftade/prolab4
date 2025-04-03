package com.education.prolab4_3.odemeYontemleri;

public class Nakit implements OdemeYontemi {
    @Override
    public boolean indirimUygulanabilirMi() {
        return false;
    }



    @Override
    public double ucretCarpani() {
        return 1.2; // Nakit ile ödeme %20 fazla
    }
}
