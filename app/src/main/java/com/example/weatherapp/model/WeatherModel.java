package com.example.weatherapp.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherModel{

    private  String day;
    private String temperature;
    private String icon;
    private String windSpeed;

    public WeatherModel(String newDay, String newTemperature, String newIcon, String newWindSpeed){
        day = newDay;

        temperature = newTemperature;
        icon = newIcon;
        windSpeed = newWindSpeed;
    }

    public String getDay() {
        return day;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getIcon() {
        return icon;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }
}