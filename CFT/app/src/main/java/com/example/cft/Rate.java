package com.example.cft;

public class Rate {//класс для валют в списке в активити
    private String Name;
    private Double Rate;
    private Integer Nominal;

    public Rate(String name, Double rate, Integer nominale) {
        Name = name;
        Rate = rate;
        Nominal = nominale;
    }

    public String getName() {
        return Name;
    }

    public String getRate() {
        return Rate.toString();
    }

    public String getNominale() {
        return Nominal.toString();
    }

}
