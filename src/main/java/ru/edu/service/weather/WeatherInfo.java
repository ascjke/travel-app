package ru.edu.service.weather;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.springframework.boot.jackson.JsonComponent;

import java.time.LocalDateTime;

/**
 * Weather info.
 */
@JsonComponent
public class WeatherInfo {

    private String city;

    /**
     * Short weather description
     * Like 'sunny', 'clouds', 'raining', etc
     */
    private String shortDescription;

    /**
     * Weather description.
     * Like 'broken clouds', 'heavy raining', etc
     */
    private String description;

    /**
     * Temperature.
     */
    private double temperature;

    /**
     * Temperature that fells like.
     */
    private double feelsLikeTemperature;

    /**
     * Wind speed.
     */
    private double windSpeed;

    /**
     * Pressure.
     */
    private double pressure;

    /**
     * Expiry time of weather info.
     * If current time is above expiry time then current weather info is not actual!
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiryTime;

    public static Builder builder() {
        return new Builder();
    }

    public String getCity() {
        return city;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getFeelsLikeTemperature() {
        return feelsLikeTemperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getPressure() {
        return pressure;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    // Inner class
    public static class Builder {

        private WeatherInfo obj = new WeatherInfo();

        public Builder setCity(String city) {
            obj.city = city;
            return this;
        }

        public Builder setShortDescription(String shortDescription) {
            obj.shortDescription = shortDescription;
            return this;
        }

        public Builder setDescription(String description) {
            obj.description = description;
            return this;
        }

        public Builder setTemperature(double temperature) {
            obj.temperature = temperature;
            return this;
        }

        public Builder setFeelsLikeTemperature(double feelsLikeTemperature) {
            obj.feelsLikeTemperature = feelsLikeTemperature;
            return this;
        }

        public Builder setWindSpeed(double windSpeed) {
            obj.windSpeed = windSpeed;
            return this;
        }

        public Builder setPressure(double pressure) {
            obj.pressure = pressure;
            return this;
        }

        public Builder setExpiryTime(LocalDateTime expiryTime) {
            obj.expiryTime = expiryTime;
            return this;
        }

        public WeatherInfo build() {
            return obj;
        }
    }

    @Override
    public String toString() {
        return "WeatherInfo{" +
                "city='" + city + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", temperature=" + temperature +
                ", feelsLikeTemperature=" + feelsLikeTemperature +
                ", windSpeed=" + windSpeed +
                ", pressure=" + pressure +
                ", expiryTime=" + expiryTime +
                '}';
    }
}
