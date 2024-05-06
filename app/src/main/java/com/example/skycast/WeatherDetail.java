package com.example.skycast;

public class WeatherDetail {
    private String title;
    private String value;

    public WeatherDetail(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }
}
