package com.education.prolab4_3.odemeYontemleri;

public class KrediKart implements OdemeYontemi {
    @Override
    public boolean indirimUygulanabilirMi() {
        return false;
    }

    @Override
    public double ucretCarpani() {
        return 1.5; // Kredi Kartı ile ödeme %50 fazla
    }
}
