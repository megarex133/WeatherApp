package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.model.WeatherAdapter;
import com.example.weatherapp.model.WeatherModel;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    private String cityName;

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
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
           ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
               ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
           }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        cityName = getCityName(location.getLongitude(), location.getLatitude());

        getWeatherByCity(cityName);

        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = cityEditText.getText().toString();
                if (city.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please, enter city",Toast.LENGTH_LONG).show();
                }
                else{
                    cityEditText.setText(cityName);
                    getWeatherByCity(city);
                }
            }
        });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CODE){
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this,"Please provide the permission", Toast.LENGTH_SHORT).show();
                    finish();
                }

        }
    }

    private String getCityName(double longitude, double latitude){
        String cityName = "Not found";
        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        try{
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 10);

            for(Address address : addressList){
                if(address!=null){
                    String city = address.getLocality();
                    if(city!=null && !city.equals("")){
                        cityName = city;
                    } else {
                        Log.d("TAG", "CITY NOT FOUND");
                        Toast.makeText(this, "User city not found...", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        } catch(IOException e){
            e.printStackTrace();
        }

        return cityName;
    }

    private void getWeatherByCity(String cityName){
        String URL ="http://api.weatherapi.com/v1/forecast.json?key=6116f19ace584395854131749221005&q="+ cityName +
                    "&days=1&aqi=yes&alerts=yes";
        cityEditText.setText(cityName);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingProgressBar.setVisibility(View.GONE);
                homeRelativeLayout.setVisibility(View.VISIBLE);
                weatherModelArrayList.clear();
                try{
                    String temperature = response.getJSONObject("current").getString("temp_c");
                    temperatureTextView.setText(temperature+"°C");
                    int isDay = response.getJSONObject("current").getInt("is_day");

                    String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("http:".concat(conditionIcon)).into(iconImageView);
                    conditionTextView.setText(condition);

                //TODO:copy 1:02:46 from video

                    try{
                        JSONObject forecast = response.getJSONObject("forecast");
                        JSONArray dailyForecast = forecast.getJSONArray("forecastday");
                        for (int i = 0; i < dailyForecast.length();i++){
                            JSONObject dayObj = dailyForecast.getJSONObject(i);
                            String day = dayObj.getString("date");
                            String temper = dayObj.getJSONObject("day").getString("avgtemp_c");
                            String img = dayObj.getJSONObject("day").getJSONObject("condition").getString("icon");
                            String wind = dayObj.getJSONObject("day").getString("maxwind_kph");
                            weatherModelArrayList.add(new WeatherModel(day,temper,img,wind));
                        }
                } catch(JSONException e){
                    e.printStackTrace();
                }
                weatherAdapter.notifyDataSetChanged();
                }


                } catch(Exception e){
                    e.printStackTrace();
                }
                }

            }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "please enter valid city name", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        requestQueue.add(jsonObject);



}