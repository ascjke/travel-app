package ru.edu.service.weather;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

/**
 * Weather provider
 */
@Component
public class WeatherProvider {

    private RestTemplate restTemplate;
    private String appKey;
    private ObjectMapper mapper = new ObjectMapper();

    @Value("${weather.providerAppkey}")
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Download ACTUAL weather info from internet.
     * You should call GET http://api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}
     * You should use Spring Rest Template for calling requests
     *
     * @param city - city
     * @return weather info or null
     */
    public WeatherInfo get(String city) {

        JsonNode node = null;
        String info;
        String uri = "http://api.openweathermap.org/data/2.5/weather?q=" + city.toLowerCase() + "&appid=" + appKey + "&units=metric";

        try {
            info = restTemplate.getForObject(uri, String.class);
        } catch (Exception ex) {
            System.out.println("Error = " + ex + "\ncity " + city + " not found");
            return null;
        }

        try {
            node = mapper.readTree(info);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String cityRequested = node.get("name").asText().toLowerCase();
        String shortDescription = node.get("weather").get(0).get("main").asText();
        String description = node.get("weather").get(0).get("description").asText();
        double temperature = node.get("main").get("temp").asDouble();
        double feelsLikeTemperature = node.get("main").get("feels_like").asDouble();
        double windSpeed = node.get("wind").get("speed").asDouble();
        double pressure = node.get("main").get("pressure").asDouble();
        LocalDateTime requestTime = LocalDateTime.now();

        return WeatherInfo.builder()
                .setCity(cityRequested)
                .setShortDescription(shortDescription)
                .setDescription(description)
                .setTemperature(temperature)
                .setFeelsLikeTemperature(feelsLikeTemperature)
                .setWindSpeed(windSpeed)
                .setPressure(pressure)
                .setExpiryTime(requestTime.plusMinutes(5)) // Here expiry time is determine at the stage of the constructor
                .build();
    }
}
