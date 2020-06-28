package com.abhishek.amplyweather.Network;

public class Weather {
    public String getLowTemperature() {
        return lowTemperature;
    }

    public void setLowTemperature(String lowTemperature) {
        this.lowTemperature = lowTemperature;
    }

    public String getHighTemperature() {
        return HighTemperature;
    }

    public void setHighTemperature(String highTemperature) {
        HighTemperature = highTemperature;
    }

    public String getComfort() {
        return comfort;
    }

    public void setComfort(String comfort) {
        this.comfort = comfort;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    String lowTemperature;
    String HighTemperature;
    String comfort;
    String state;
    String city;


}
