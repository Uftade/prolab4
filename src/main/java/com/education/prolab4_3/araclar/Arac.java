package com.education.prolab4_3.araclar;

import com.education.prolab4_3.yolcuTipi.Yolcu;

public abstract class Arac {
    // Verilen temel ücrete göre, yolcuya uygulanacak fiyatı hesaplar.
    // yolcu parametresi, indirim durumunun belirlenmesinde kullanılır.
    public abstract double fiyatHesapla(double temelUcret, Yolcu yolcu);
}