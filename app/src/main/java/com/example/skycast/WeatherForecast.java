package com.example.skycast;

import java.util.ArrayList;

public class WeatherForecast {
    private String locationName;
    private String todayForecastSummary;
    private ArrayList<WeatherDetail> forecastDetails;

    public WeatherForecast(String locationName, String todayForecastSummary, ArrayList<WeatherDetail> forecastDetails) {
        this.locationName = locationName;
        this.todayForecastSummary = todayForecastSummary;
        this.forecastDetails = forecastDetails;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getTodayForecastSummary() {
        return todayForecastSummary;
    }

    public ArrayList<WeatherDetail> getForecastDetails() {
        return forecastDetails;
    }

    // You can add more methods here if needed
}
