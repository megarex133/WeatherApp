package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.weatherapp.model.WeatherAdapter;
import com.example.weatherapp.model.WeatherModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout homeRelativeLayout;
    private ProgressBar loadingProgressBar;
    private TextView cityNameTextView, temperatureTextView, conditionTextView;
    private RecyclerView weatherRecycleView;
    private TextInputEditText cityEditText;
    private ImageView backImageView, iconImageView, searchImageView;
    private ArrayList<WeatherModel> weatherModelArrayList;
    private WeatherAdapter weatherAdapter;
    private LocationManager locationManager;
    private final int PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);
        homeRelativeLayout = findViewById((R.id.idRLHome));
        loadingProgressBar = findViewById((R.id.idPBLoading));
        cityNameTextView = findViewById((R.id.idTVCityName));
        temperatureTextView = findViewById((R.id.idTVTemperature));
        conditionTextView = findViewById((R.id.idTVCondition));
        weatherRecycleView = findViewById((R.id.idRvWeather));
        cityEditText = findViewById((R.id.idEditCity));
        backImageView = findViewById((R.id.idRvWeather));
        iconImageView = findViewById((R.id.idIVIcon));
        searchImageView = findViewById((R.id.idIVSearch));
        weatherModelArrayList = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(this,weatherModelArrayList);
        weatherRecycleView.setAdapter(weatherAdapter);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(this<Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTER && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!))

    }

    private void getWeatherByCity(String cityName){
        String URL ="http://api.weatherapi.com/v1/forecast.json?key=6116f19ace584395854131749221005&q="+ cityName +
                    "&days=1&aqi=yes&alerts=yes";
    }
}