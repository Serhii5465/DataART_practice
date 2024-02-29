package com.telegram_bot.service;

import java.util.Locale;
import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.OpenWeatherMap;
import java.text.SimpleDateFormat;

public class Weather {

    private final String key = "";
    private OpenWeatherMap openWeatherMap = null;
    private CurrentWeather currentWeather = null;


    public Weather() {
        openWeatherMap = new OpenWeatherMap(key);
    }

    public String getKey() {
        return key;
    }

    public OpenWeatherMap getOpenWeatherMap() {
        return openWeatherMap;
    }

    public void setOpenWeatherMap(OpenWeatherMap openWeatherMap) {
        this.openWeatherMap = openWeatherMap;
    }

    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(CurrentWeather currentWeather) {
        this.currentWeather = currentWeather;
    }

    /**
     * @param temp_Fahren
     * @return value degree in Celsius
     */

    public float convertFahrenheitToCelsius(float temp_Fahren) {
        return (temp_Fahren - 32) * 5 / 9;
    }

    /**
     * 
     * @return data of weather for the established city
     */
    public String getWeather() {
        StringBuilder builder = new StringBuilder();

        builder.append(new SimpleDateFormat("E dd/MM", Locale.ENGLISH).format(currentWeather.getDateTime()) + "\n");
        builder.append("Sunrise: " + new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(currentWeather.getSysInstance().getSunriseTime()) + "\n");
        builder.append("Sunset: " + new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(currentWeather.getSysInstance().getSunsetTime()) + "\n\n");
        builder.append("Humidity: " + Math.round(currentWeather.getMainInstance().getHumidity()) + "%\n");
        builder.append("Cloudiness: " + Math.round(currentWeather.getCloudsInstance().getPercentageOfClouds()) + "%\n");
        builder.append("Temperature: " + Math.round(convertFahrenheitToCelsius(currentWeather.getMainInstance().getTemperature())) + "\u00b0 C\n");
        return builder.toString();
    }
}