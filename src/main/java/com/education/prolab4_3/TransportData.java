package com.education.prolab4_3;

import com.education.prolab4_3.araclar.Taksi;

import java.util.List;

public class TransportData {
    private String city;
    private Taksi taxi;
    private List<Stop> duraklar;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Stop> getDuraklar() {
        return duraklar;
    }

    public void setDuraklar(List<Stop> duraklar) {
        this.duraklar = duraklar;
    }

    public Taksi getTaxi() {
        return taxi;
    }

    public void setTaxi(Taksi taxi) {
        this.taxi = taxi;
    }
}
