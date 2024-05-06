package com.example.skycast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class LatestObservationAdapter extends ArrayAdapter<WeatherObservation> {

    public LatestObservationAdapter(LatestObservationActivity context, ArrayList<WeatherObservation> observations) {
        super(context, 0, observations);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.latestobservation_list_item, parent, false);
        }

        WeatherObservation currentObservation = getItem(position);

        TextView titleTextView = listItemView.findViewById(R.id.titleTextView);
        titleTextView.setText(currentObservation.getTitle());

        TextView temperatureTextView = listItemView.findViewById(R.id.temperatureTextView);
        temperatureTextView.setText("Temperature: " + currentObservation.getTemperature());

        TextView windTextView = listItemView.findViewById(R.id.windTextView);
        windTextView.setText("Wind: " + currentObservation.getWind());

        TextView humidityTextView = listItemView.findViewById(R.id.humidityTextView);
        humidityTextView.setText("Humidity: " + currentObservation.getHumidity());

        TextView pressureTextView = listItemView.findViewById(R.id.pressureTextView);
        pressureTextView.setText("Pressure: " + currentObservation.getPressure());

        TextView visibilityTextView = listItemView.findViewById(R.id.visibilityTextView);
        visibilityTextView.setText("Visibility: " + currentObservation.getVisibility());

        TextView pubDateTextView = listItemView.findViewById(R.id.pubDateTextView);
        pubDateTextView.setText(currentObservation.getPubDate());

        return listItemView;
    }
}
