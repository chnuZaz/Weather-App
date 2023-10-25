package com.example.weather_me;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    private LinearLayout homeRL;
    RelativeLayout backRL;
    private ProgressBar loadingPB;
    private TextView cityNameTV, temperatureTV, conditionTV;
    private RecyclerView weatherRV;
    private TextInputEditText cityEdt;
    private ImageView backIV, iconIV, searchIV;
    private ArrayList<RVModel> weatherRvModelArrayList;

    private WeatherRvAdapter weatherRvAdapter;
    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;
    private String cityName;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_main);
        homeRL = findViewById(R.id.idRLHome);
        loadingPB = findViewById(R.id.idPBLoading);
        cityNameTV = findViewById(R.id.idTVCityName);
        temperatureTV = findViewById(R.id.idTVTemperature);
        conditionTV = findViewById(R.id.idTVCondition);
        backRL=findViewById(R.id.idRLBack);
        weatherRV = findViewById(R.id.idRVWeather);
        cityEdt = findViewById(R.id.idEdtCity);
        backIV = findViewById(R.id.idIVBack);
        iconIV = findViewById(R.id.idIVIcon);
        searchIV = findViewById(R.id.idIVSearch);
        weatherRvModelArrayList = new ArrayList<>();
        weatherRvAdapter = new WeatherRvAdapter(this, weatherRvModelArrayList);
        weatherRV.setAdapter(weatherRvAdapter);
        weatherRV.setLayoutManager(new LinearLayoutManager(this));

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
           @Override
            public void onLocationChanged(Location location) {
                //cityName = getCityName(location.getLongitude(), location.getLatitude());
                //getWeatherInfo(cityName);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                //cityName = getCityName(lastKnownLocation.getLongitude(), lastKnownLocation.getLatitude());
                getWeatherInfo(cityName);
            }
        }

        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = cityEdt.getText().toString();
                if (city.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please Enter City Name", Toast.LENGTH_SHORT).show();
                } else {
                    cityNameTV.setText(city);
                    getWeatherInfo(city);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            } else {
                Toast.makeText(this, "Please provide the Permission", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    /*public String getCityName(double longitude, double latitude) {
        String cityName = "Not found";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address adr = addresses.get(0);
                if (adr != null) {
                    String city = adr.getLocality();
                    if (city != null && !city.isEmpty()) {
                        cityName = city;
                    } else {
                        Log.d("TAG", "CITY NOT FOUND");
                        cityName = "City not found";
                    }
                }
            } else {
                Log.d("TAG", "No addresses found");
                cityName = "No addresses found";
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("TAG", "IOException: " + e.getMessage());
            cityName = "Error occurred";
        }
        return cityName;
    }*/

    public void getWeatherInfo(String city) {
        String url = "https://api.weatherapi.com/v1/forecast.json?key=4863b310c3834bb3ba362704230409&q="+city+"&days=7&aqi=no&alerts=no";
        cityNameTV.setText(city);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingPB.setVisibility(View.GONE);
                homeRL.setVisibility(View.VISIBLE);
                weatherRvModelArrayList.clear();

                try {
                    String temperature = response.getJSONObject("current").getString("temp_c");
                    temperatureTV.setText(temperature + "°C");
                    int isDay = response.getJSONObject("current").getInt("is_day");
                    String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("https:".concat(conditionIcon)).into(iconIV);
                    conditionTV.setText(condition);
                    /*if (isDay == 1) {
                        //morning
                        Picasso.get().load("https://cdn.dribbble.com/users/925716/screenshots/3333720/attachments/722376/after_noon.png").into(backIV);
                    } else {
                        Picasso.get().load("https://cdn.dribbble.com/users/925716/screenshots/3333720/attachments/722375/night.png").into(backIV);
                    }*/

                    JSONObject forecastObj = response.getJSONObject("forecast");
                    JSONArray forecastdayArray = forecastObj.getJSONArray("forecastday");

                    for (int i = 0; i < forecastdayArray.length(); i++) {
                        JSONObject dayObj = forecastdayArray.getJSONObject(i);
                        String day = dayObj.getString("date");
                        String temper = dayObj.getJSONObject("day").getString("avgtemp_c");
                        String windSpeed = dayObj.getJSONObject("day").getString("maxwind_kph");
                        String icon = dayObj.getJSONObject("day").getJSONObject("condition").getString("icon");
                        String condi = dayObj.getJSONObject("day").getJSONObject("condition").getString("text");
                        weatherRvModelArrayList.add(new RVModel(day, condi,windSpeed ,temper, icon));
                    }
                    weatherRvAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
