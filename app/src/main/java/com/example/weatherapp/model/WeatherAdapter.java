package com.example.weatherapp.model;

import android.view.*;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.*;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder>{
    private Context context;
    private List<WeatherModel> weatherList;

    public WeatherAdapter(Context context, List<WeatherModel> weatherModelList) {
        this.context = context;
        this.weatherList = weatherModelList;
    }

    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewPart){
        View view = LayoutInflater.from(context).inflate(R.layout.weather_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.ViewHolder holder, int position){
        WeatherModel weather = weatherList.get(position);

        Picasso.get().load("http:".concat(weather.getIcon())).into(holder.condition);
        holder.wind.setText(weather.getWindSpeed()+"km/h");
        holder.temperature.setText(weather.getTemperature()+"°C");
        SimpleDateFormat inputDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat outputDateTime = new SimpleDateFormat("MM dd");
        holder.time.setText(weather.getDay());
//        try {
//            Date date = new Date(weather.getDay());
//
//        } catch(Exception e){
//            e.printStackTrace();
//        }
    }

    public int getItemCount(){
        return weatherList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView wind, temperature, time;
        private ImageView condition;
        public ViewHolder(@NonNull View viewItem){
            super(viewItem);
            wind = itemView.findViewById(R.id.idTVWindSpeed);
            temperature = itemView.findViewById(R.id.idTVTemperature);
            time = itemView.findViewById(R.id.idTVTime);
            condition = itemView.findViewById(R.id.idIVCondition);

        }
    }
}
