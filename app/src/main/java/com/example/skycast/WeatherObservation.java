package com.example.skycast;

import java.util.ArrayList;

public class WeatherObservation {
    private String title;
    private ArrayList<WeatherDetail> details;
    private String pubDate;

    public WeatherObservation(String title, ArrayList<WeatherDetail> details, String pubDate) {
        this.title = title;
        this.details = details;
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<WeatherDetail> getDetails() {
        return details;
    }

    public String getPubDate() {
        return pubDate;
    }

    // New methods to retrieve temperature, wind, humidity, pressure, and visibility
    public String getTemperature() {
        for (WeatherDetail detail : details) {
            if (detail.getTitle().equals("Temperature")) {
                return detail.getValue();
            }
        }
        return null;
    }

    public String getWind() {
        for (WeatherDetail detail : details) {
            if (detail.getTitle().equals("Wind")) {
                return detail.getValue();
            }
        }
        return null;
    }

    public String getHumidity() {
        for (WeatherDetail detail : details) {
            if (detail.getTitle().equals("Humidity")) {
                return detail.getValue();
            }
        }
        return null;
    }

    public String getPressure() {
        for (WeatherDetail detail : details) {
            if (detail.getTitle().equals("Pressure")) {
                return detail.getValue();
            }
        }
        return null;
    }

    public String getVisibility() {
        for (WeatherDetail detail : details) {
            if (detail.getTitle().equals("Visibility")) {
                return detail.getValue();
            }
        }
        return null;
    }
}
