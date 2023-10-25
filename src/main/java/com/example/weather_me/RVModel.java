package com.example.weather_me;

public class RVModel {

    String  Day;

    String Condition;

    String WindSpeed;

    String Temperature;

    String Image;

    public RVModel(String day, String condition, String windSpeed, String temperature, String image) {
        Day = day;
        Condition = condition;
        WindSpeed = windSpeed;
        Temperature = temperature;
        Image = image;
    }

    public String getDay() {
        return Day;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getCondition() {
        return Condition;
    }

    public void setCondition(String condition) {
        Condition = condition;
    }

    public String getWindSpeed() {
        return WindSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        WindSpeed = windSpeed;
    }

    public String getTemperature() {
        return Temperature;
    }

    public void setTemperature(String temperature) {
        Temperature = temperature;
    }
}
